package com.tstartup.tserver.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.api.client.util.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tstartup.tserver.common.PageVo;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.enums.CommonTypeEnum;
import com.tstartup.tserver.persistence.dataobject.*;
import com.tstartup.tserver.persistence.mapper.TArticleDao;
import com.tstartup.tserver.persistence.service.*;
import com.tstartup.tserver.util.DateUtil;
import com.tstartup.tserver.util.PageUtil;
import com.tstartup.tserver.web.dto.*;
import com.tstartup.tserver.web.dto.article.ArticleItemSimpleDto;
import com.tstartup.tserver.web.vo.SceneSimpleItemDto;
import com.tstartup.tserver.web.vo.SourceItemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName ArticleBusService
 * @Description
 * @Author zhang
 * @Date 2024/8/17 17:39
 * @Version 1.0
 */
@Service
public class ArticleBusService {

    @Resource
    private TArticleService articleService;

    @Resource
    private TArticleTypeRelationService articleTypeRelationService;

    @Resource
    private TCommonTypeService commonTypeService;

    @Resource
    private TSceneService sceneService;

    @Resource
    private TCityService cityService;

    @Resource
    private TArticleDao articleDao;

    @Transactional
    public ApiResponse update(ArticleUpdateDto updateDto) {
        Integer id = updateDto.getId();
        if (Objects.isNull(id)) {
            TArticle tArticle = new TArticle();

            BeanUtils.copyProperties(updateDto, tArticle);

            List<Integer> keywordIdList = updateDto.getKeywordIdList();
            List<Integer> tagIdList = updateDto.getTagIdList();
            //List<Integer> cityIdList = updateDto.getCityIdList();
            List<Integer> sceneIdList = updateDto.getSceneIdList();
            List<Integer> tripTypeIdList = updateDto.getTripTypeIdList();
            List<Integer> articleTypeIdList = updateDto.getArticleTypeIdList();

            SourceItemVo sourceVo = updateDto.getSourceVo();
            tArticle.setSource(Objects.nonNull(sourceVo) ? JSON.toJSONString(sourceVo) : "");

            tArticle.setCreateTime(DateUtil.getNowSeconds());
            articleService.save(tArticle);

            Integer articleId = tArticle.getId();

            processAddCommonType(keywordIdList, articleId, CommonTypeEnum.KEYWORD);
            processAddCommonType(tagIdList, articleId, CommonTypeEnum.TAG);
            //processAddCommonType(cityIdList, articleId, CommonTypeEnum.CITY);
            processAddCommonType(sceneIdList, articleId, CommonTypeEnum.SCENE_ID);
            processAddCommonType(tripTypeIdList, articleId, CommonTypeEnum.TRIP_TYPE);
            processAddCommonType(articleTypeIdList, articleId, CommonTypeEnum.ARTICLE_TYPE);


            return ApiResponse.newSuccess();
        } else {
            TArticle tArticle = articleService.getById(id);
            if (Objects.isNull(tArticle)) {
                return ApiResponse.newParamError();
            }

            BeanUtils.copyProperties(updateDto, tArticle);

            SourceItemVo sourceVo = updateDto.getSourceVo();
            tArticle.setSource(Objects.nonNull(sourceVo) ? JSON.toJSONString(sourceVo) : "");

            tArticle.setUpdateTime(DateUtil.getNowSeconds());
            Integer articleId = tArticle.getId();
            //List<String> keywords = updateDto.getKeywords();
            //tArticle.setKeywords(CollectionUtils.isEmpty(keywords) ? "" : JSON.toJSONString(keywords));


            articleService.updateById(tArticle);

            List<Integer> keywordIdList = updateDto.getKeywordIdList();
            List<Integer> tagIdList = updateDto.getTagIdList();
            //List<Integer> cityIdList = updateDto.getCityIdList();
            List<Integer> sceneIdList = updateDto.getSceneIdList();
            List<Integer> tripTypeIdList = updateDto.getTripTypeIdList();
            List<Integer> articleTypeIdList = updateDto.getArticleTypeIdList();

            List<TArticleTypeRelation> typeRelationList = articleTypeRelationService.list(Wrappers.<TArticleTypeRelation>lambdaQuery()
                    .eq(TArticleTypeRelation::getArticleId, tArticle.getId()));

            processUpdateCommonType(keywordIdList, articleId, CommonTypeEnum.KEYWORD, typeRelationList);
            processUpdateCommonType(tripTypeIdList, articleId, CommonTypeEnum.TRIP_TYPE, typeRelationList);
            processUpdateCommonType(tagIdList, articleId, CommonTypeEnum.TAG, typeRelationList);
            //processUpdateCommonType(cityIdList, articleId, CommonTypeEnum.CITY, typeRelationList);
            processUpdateCommonType(sceneIdList, articleId, CommonTypeEnum.SCENE_ID, typeRelationList);
            processUpdateCommonType(articleTypeIdList, articleId, CommonTypeEnum.ARTICLE_TYPE, typeRelationList);

            return ApiResponse.newSuccess();
        }
    }

    private void processAddCommonType(List<Integer> typeIdList, Integer articleId, CommonTypeEnum typeEnum) {
        if (CollectionUtils.isEmpty(typeIdList)) {
            return;
        }
        typeIdList.forEach(typeId -> {
            TArticleTypeRelation typeRelation = new TArticleTypeRelation();
            typeRelation.setTypeId(typeId);
            typeRelation.setArticleId(articleId);
            typeRelation.setTypeIdentity(typeEnum.getType());
            typeRelation.setCreateTime(DateUtil.getNowSeconds());

            articleTypeRelationService.save(typeRelation);
        });
    }

    private void processUpdateCommonType(List<Integer> typeIdList, Integer articleId, CommonTypeEnum typeEnum, List<TArticleTypeRelation> relationList) {
        typeIdList = CollectionUtils.isEmpty(typeIdList) ? Lists.newArrayList() : typeIdList;
        List<TArticleTypeRelation> typeRelationList = relationList.stream().filter(e -> e.getTypeIdentity().equals(typeEnum.getType())).collect(Collectors.toList());
        List<Integer> typeIdListDb = typeRelationList.stream().map(TArticleTypeRelation::getTypeId).collect(Collectors.toList());;

        if (CollectionUtils.isEmpty(typeIdList)) {
            if (!CollectionUtils.isEmpty(typeRelationList)) {
                typeRelationList.forEach(tArticleTypeRelation -> {
                    articleTypeRelationService.removeById(tArticleTypeRelation.getId());
                });
            }
        }

        List<Integer> typeIdListInsert = typeIdList.stream().filter(e -> !typeIdListDb.contains(e)).collect(Collectors.toList());

        List<Integer> finalTypeIdList = typeIdList;
        List<Integer> typeIdListDel = typeIdListDb.stream().filter(e -> !finalTypeIdList.contains(e)).collect(Collectors.toList());

        typeIdListInsert.forEach(typeId -> {
            TArticleTypeRelation typeRelation = new TArticleTypeRelation();
            typeRelation.setTypeId(typeId);
            typeRelation.setArticleId(articleId);
            typeRelation.setTypeIdentity(typeEnum.getType());
            typeRelation.setCreateTime(DateUtil.getNowSeconds());

            articleTypeRelationService.save(typeRelation);
        });

        if (!CollectionUtils.isEmpty(typeIdListDel)) {
            articleTypeRelationService.remove(Wrappers.<TArticleTypeRelation>lambdaQuery()
                    .eq(TArticleTypeRelation::getArticleId, articleId)
                    .in(TArticleTypeRelation::getTypeId, typeIdListDel));
        }
    }

    public ApiResponse<PageVo<ArticleItemSimpleDto>> homeList(ArticleHomePageQryDto pageQryDto) {
        int pageNo = pageQryDto.getPageNo();
        pageNo = pageNo <= 0 ? 1 : pageNo;
        int pageSize = pageQryDto.getPageSize();
        pageSize = pageSize <= 0 ? 10 : pageSize;


        Integer countryId = pageQryDto.getCountryId();
        Integer cityId = pageQryDto.getCityId();
        Integer tripTypeId = pageQryDto.getTripTypeId();
        Integer isHot = pageQryDto.getIsHot();
        Integer articleTypeId = pageQryDto.getArticleTypeId();


        PageVo<ArticleItemSimpleDto> pageVo = new PageVo<>();
        if (Objects.isNull(countryId) && Objects.isNull(cityId) && Objects.isNull(tripTypeId) && Objects.isNull(articleTypeId)) {
            LambdaQueryWrapper<TArticle> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TArticle::getIsDelete, 0)
                    .eq(TArticle::getStatus, 4)
                    .eq(TArticle::getIsHot, isHot)

                    .orderByDesc(TArticle::getId);

            Page<TArticle> page = articleService.page(new Page<>(pageNo, pageSize), queryWrapper);
            pageVo = PageUtil.getPageVo(page, (e) -> {
                ArticleItemSimpleDto vo = new ArticleItemSimpleDto();
                BeanUtils.copyProperties(e, vo);

                return vo;
            });
            pageVo.setTotal(page.getTotal());

        } else {

            Integer start = (pageNo - 1) * pageSize;
            List<TArticle> tArticleList = articleDao.queryArticleList(articleTypeId, tripTypeId, countryId, cityId, isHot, start, pageSize);
            if (!CollectionUtils.isEmpty(tArticleList)) {
                List<ArticleItemSimpleDto> articleItemDtoList = tArticleList.stream().map(tArticle -> {
                    ArticleItemSimpleDto vo = new ArticleItemSimpleDto();
                    BeanUtils.copyProperties(tArticle, vo);

                    String source = tArticle.getSource();
                    if (!Strings.isNullOrEmpty(source)) {
                        SourceItemVo sourceItemVo = JSON.parseObject(source, SourceItemVo.class);
                        sourceItemVo.setUrl("");
                        vo.setSource(sourceItemVo);
                    }

                    return vo;
                }).collect(Collectors.toList());

                Integer cnt = articleDao.queryArticleCnt(articleTypeId, tripTypeId, countryId, cityId, isHot);

                pageVo.setRecords(articleItemDtoList);
                pageVo.setTotal(Long.valueOf(cnt));
            }
        }

        List<ArticleItemSimpleDto> recordList = pageVo.getRecords();

        /*buildCommonTypeItemList(recordList, CommonTypeEnum.KEYWORD);
        buildCommonTypeItemList(recordList, CommonTypeEnum.TAG);
        buildCommonTypeItemList(recordList, CommonTypeEnum.ARTICLE_TYPE);
        buildCommonTypeItemList(recordList, CommonTypeEnum.TRIP_TYPE);
        buildCommonTypeItemList(recordList, CommonTypeEnum.SCENE_ID);

        List<Integer> countryCityIdList = new ArrayList<>();
        List<Integer> countryIdList = recordList.stream().map(ArticleItemDto::getDestCountry).distinct().toList();
        List<Integer> cityIdList = recordList.stream().map(ArticleItemDto::getDestCity).distinct().toList();
        countryCityIdList.addAll(countryIdList);
        countryCityIdList.addAll(cityIdList);


        Map<Integer, String> countryIdNameMap = getCountryIdNameMap(countryCityIdList);

        recordList.forEach(articleItemDto -> {
            Integer destCountry = articleItemDto.getDestCountry();
            Integer destCity = articleItemDto.getDestCity();

            if (Objects.nonNull(destCountry)) {
                articleItemDto.setCountryName(countryIdNameMap.get(destCountry));
            }
            if (Objects.nonNull(destCity)) {
                articleItemDto.setCityName(countryIdNameMap.get(destCity));
            }
        });*/


        PageArticleVo<ArticleItemSimpleDto> pageArticleVo = new PageArticleVo();
        pageArticleVo.setRecords(recordList);
        pageArticleVo.setTotal(pageVo.getTotal());

        return ApiResponse.newSuccess(pageVo);

    }

    private Map<Integer, String> getCountryIdNameMap(List<Integer> countryCityIdList) {
        if (CollectionUtils.isEmpty(countryCityIdList)) {
            return Maps.newHashMap();
        }
        List<TCity> cityList = cityService.list(Wrappers.<TCity>lambdaQuery().in(TCity::getId, countryCityIdList));
        return cityList.stream().collect(Collectors.toMap(TCity::getId, TCity::getName, (k1, k2) -> k1));
    }

    public ApiResponse<ArticleItemDto> detail(CommonIdDto commonIdDto) {
        ArticleItemDto articleItemDto = new ArticleItemDto();

        Integer id = commonIdDto.getId();

        TArticle tArticle = articleService.getById(id);
        if (Objects.isNull(tArticle)) {
            return ApiResponse.newParamError("not exist");
        }
        BeanUtils.copyProperties(tArticle, articleItemDto);

        String source = tArticle.getSource();
        if (!Strings.isNullOrEmpty(source)) {
            SourceItemVo sourceItemVo = JSON.parseObject(source, SourceItemVo.class);
            articleItemDto.setSource(sourceItemVo);
        }

        List<ArticleItemDto> recordList = Arrays.asList(articleItemDto);
        buildCommonTypeItemList(recordList, CommonTypeEnum.KEYWORD);
        buildCommonTypeItemList(recordList, CommonTypeEnum.TAG);
        buildCommonTypeItemList(recordList, CommonTypeEnum.ARTICLE_TYPE);
        buildCommonTypeItemList(recordList, CommonTypeEnum.TRIP_TYPE);
        buildCommonTypeItemList(recordList, CommonTypeEnum.SCENE_ID);

        ArticleItemDto itemDto = recordList.get(0);

        return ApiResponse.newSuccess(itemDto);
    }

    public ApiResponse<PageArticleVo> listByAdmin(ArticlePageQryDto pageQryDto) {
        Integer countryId = pageQryDto.getCountryId();
        Integer articleTypeId = pageQryDto.getArticleTypeId();
        String headline = pageQryDto.getHeadline();
        Integer pageNo = pageQryDto.getPageNo();
        Integer pageSize = pageQryDto.getPageSize();
        /*LambdaQueryWrapper<TArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TArticle::getIsDelete, 0);
        queryWrapper.ne(TArticle::getStatus, 5);
        queryWrapper.orderByDesc(TArticle::getId);*/


        PageVo<ArticleItemDto> pageVo = new PageVo<>();

        Integer start = (pageNo - 1) * pageSize;
        List<TArticle> tArticleList = articleDao.queryArticleAdminList(countryId, articleTypeId, headline, start, pageSize);
        if (!CollectionUtils.isEmpty(tArticleList)) {
            List<ArticleItemDto> articleItemDtoList = tArticleList.stream().map(tArticle -> {
                ArticleItemDto vo = new ArticleItemDto();
                BeanUtils.copyProperties(tArticle, vo);

                String source = tArticle.getSource();
                if (!Strings.isNullOrEmpty(source)) {
                    SourceItemVo sourceItemVo = JSON.parseObject(source, SourceItemVo.class);
                    vo.setSource(sourceItemVo);
                }

                return vo;
            }).collect(Collectors.toList());

            Integer cnt = articleDao.queryArticleAdminCnt(countryId, articleTypeId, headline);

            pageVo.setRecords(articleItemDtoList);
            pageVo.setTotal(Long.valueOf(cnt));
        }

        List<ArticleItemDto> recordList = pageVo.getRecords();
        List<Integer> countryCityIdList = new ArrayList<>();
        List<Integer> countryIdList = recordList.stream().map(ArticleItemDto::getDestCountry).distinct().toList();
        List<Integer> cityIdList = recordList.stream().map(ArticleItemDto::getDestCity).distinct().toList();
        countryCityIdList.addAll(countryIdList);
        countryCityIdList.addAll(cityIdList);


        Map<Integer, String> countryIdNameMap = getCountryIdNameMap(countryCityIdList);

        recordList.forEach(articleItemDto -> {
            Integer destCountry = articleItemDto.getDestCountry();
            Integer destCity = articleItemDto.getDestCity();

            if (Objects.nonNull(destCountry)) {
                articleItemDto.setCountryName(countryIdNameMap.get(destCountry));
            }
            if (Objects.nonNull(destCity)) {
                articleItemDto.setCityName(countryIdNameMap.get(destCity));
            }
        });

        buildCommonTypeItemList(recordList, CommonTypeEnum.KEYWORD);
        buildCommonTypeItemList(recordList, CommonTypeEnum.TAG);
        buildCommonTypeItemList(recordList, CommonTypeEnum.ARTICLE_TYPE);
        buildCommonTypeItemList(recordList, CommonTypeEnum.TRIP_TYPE);
        buildCommonTypeItemList(recordList, CommonTypeEnum.SCENE_ID);

        PageArticleVo pageArticleVo = new PageArticleVo();
        pageArticleVo.setRecords(recordList);
        pageArticleVo.setTotal(pageVo.getTotal());

        return ApiResponse.newSuccess(pageArticleVo);
    }

    public ApiResponse<List<ArticleItemDto>> getListBySceneId(CommonIdDto commonIdDto) {
        Integer id = commonIdDto.getId();

        List<TArticleTypeRelation> typeRelationList = articleTypeRelationService.list(Wrappers.<TArticleTypeRelation>lambdaQuery()
                .eq(TArticleTypeRelation::getTypeId, id)
                .eq(TArticleTypeRelation::getTypeIdentity, CommonTypeEnum.SCENE_ID.getType())
                .orderByDesc(TArticleTypeRelation::getId)
                .last("limit 100"));

        List<Integer> articleIdList = typeRelationList.stream().map(TArticleTypeRelation::getArticleId).distinct().toList();
        if (articleIdList.size() > 30) {
            articleIdList = articleIdList.subList(0, 30);
        }
        if (CollectionUtils.isEmpty(articleIdList)) {
            return ApiResponse.newSuccess();
        }
        List<TArticle> articleList = articleService.list(Wrappers.<TArticle>lambdaQuery()
                .in(TArticle::getId, articleIdList)
                .orderByDesc(TArticle::getId));
        List<ArticleItemDto> articleItemDtoList = buildArticleItemList(articleList);

        return ApiResponse.newSuccess(articleItemDtoList);
    }

    /**
     * build article
     * @param tArticleList
     * @return
     */
    private List<ArticleItemDto> buildArticleItemList(List<TArticle> tArticleList) {
        List<ArticleItemDto> articleItemDtoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tArticleList)) {
            articleItemDtoList = tArticleList.stream().map(tArticle -> {
                ArticleItemDto vo = new ArticleItemDto();
                BeanUtils.copyProperties(tArticle, vo);

                String source = tArticle.getSource();
                if (!Strings.isNullOrEmpty(source)) {
                    SourceItemVo sourceItemVo = JSON.parseObject(source, SourceItemVo.class);
                    vo.setSource(sourceItemVo);
                }

                return vo;
            }).collect(Collectors.toList());

        }

        List<Integer> countryCityIdList = new ArrayList<>();
        List<Integer> countryIdList = tArticleList.stream().map(TArticle::getDestCountry).distinct().toList();
        List<Integer> cityIdList = tArticleList.stream().map(TArticle::getDestCity).distinct().toList();
        countryCityIdList.addAll(countryIdList);
        countryCityIdList.addAll(cityIdList);


        Map<Integer, String> countryIdNameMap = getCountryIdNameMap(countryCityIdList);

        articleItemDtoList.forEach(articleItemDto -> {
            Integer destCountry = articleItemDto.getDestCountry();
            Integer destCity = articleItemDto.getDestCity();

            if (Objects.nonNull(destCountry)) {
                articleItemDto.setCountryName(countryIdNameMap.get(destCountry));
            }
            if (Objects.nonNull(destCity)) {
                articleItemDto.setCityName(countryIdNameMap.get(destCity));
            }
        });

        buildCommonTypeItemList(articleItemDtoList, CommonTypeEnum.KEYWORD);
        buildCommonTypeItemList(articleItemDtoList, CommonTypeEnum.TAG);
        buildCommonTypeItemList(articleItemDtoList, CommonTypeEnum.ARTICLE_TYPE);
        buildCommonTypeItemList(articleItemDtoList, CommonTypeEnum.TRIP_TYPE);
        buildCommonTypeItemList(articleItemDtoList, CommonTypeEnum.SCENE_ID);

        return articleItemDtoList;
    }


    public void buildCommonTypeItemList(List<ArticleItemDto> itemDtoList, CommonTypeEnum commonTypeEnum) {
        List<Integer> articleIdList = itemDtoList.stream().map(ArticleItemDto::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(articleIdList)) {
            List<TCommonType> commonTypeList = commonTypeService.list(Wrappers.<TCommonType>lambdaQuery().eq(TCommonType::getTypeIdentity, commonTypeEnum.getType()));
            Map<Integer, TCommonType> typeIdMap = commonTypeList.stream().collect(Collectors.toMap(TCommonType::getId, Function.identity(), (k1, k2) -> k1));

            List<TArticleTypeRelation> relationList = articleTypeRelationService.list(Wrappers.<TArticleTypeRelation>lambdaQuery().in(TArticleTypeRelation::getArticleId, articleIdList).eq(TArticleTypeRelation::getTypeIdentity, commonTypeEnum.getType()));
            Map<Integer, List<TArticleTypeRelation>> articleIdRelationMap = relationList.stream().collect(Collectors.groupingBy(TArticleTypeRelation::getArticleId));
            List<Integer> sceneIdList = relationList.stream().map(TArticleTypeRelation::getTypeId).distinct().collect(Collectors.toList());
            Map<Integer, TScene> sceneMap = Maps.newHashMap();
            if (!CollectionUtils.isEmpty(sceneIdList)) {
                List<TScene> sceneList = sceneService.list(Wrappers.<TScene>lambdaQuery().in(TScene::getId, sceneIdList));
                sceneMap = sceneList.stream().collect(Collectors.toMap(TScene::getId, Function.identity()));
            }

            Map<Integer, TScene> finalSceneMap = sceneMap;
            itemDtoList.stream().forEach(itemDto -> {
                Integer articleId = itemDto.getId();
                List<TArticleTypeRelation> typeRelationList = articleIdRelationMap.get(articleId);
                if (!CollectionUtils.isEmpty(typeRelationList)) {
                    //景点
                    if (commonTypeEnum.equals(CommonTypeEnum.SCENE_ID)) {
                        List<SceneSimpleItemDto> sceneList = typeRelationList.stream().map(e -> {
                            Integer typeId = e.getTypeId();
                            TScene tScene = finalSceneMap.get(typeId);
                            SceneSimpleItemDto commonTypeItemDto = new SceneSimpleItemDto();
                            if (Objects.nonNull(tScene)) {
                                BeanUtils.copyProperties(tScene, commonTypeItemDto);
                            }
                            return commonTypeItemDto;
                        }).collect(Collectors.toList());
                        itemDto.setSceneList(sceneList);
                    } else {
                        List<CommonTypeItemDto> typeItemDtoList = typeRelationList.stream().map(e -> {
                            Integer typeId = e.getTypeId();
                            TCommonType tCommonType = typeIdMap.get(typeId);
                            CommonTypeItemDto commonTypeItemDto = new CommonTypeItemDto();
                            if (Objects.nonNull(tCommonType)) {
                                BeanUtils.copyProperties(tCommonType, commonTypeItemDto);
                                commonTypeItemDto.setDesc(tCommonType.getDescription());
                            }
                            return commonTypeItemDto;
                        }).collect(Collectors.toList());

                        if (commonTypeEnum.equals(CommonTypeEnum.KEYWORD)) {
                            itemDto.setKeywordList(typeItemDtoList);
                        }
                        if (commonTypeEnum.equals(CommonTypeEnum.TAG)) {
                            itemDto.setTagList(typeItemDtoList);
                        }
                        if (commonTypeEnum.equals(CommonTypeEnum.ARTICLE_TYPE)) {
                            itemDto.setArticleTypeList(typeItemDtoList);
                        }
                        if (commonTypeEnum.equals(CommonTypeEnum.TRIP_TYPE)) {
                            itemDto.setTripTypeList(typeItemDtoList);
                        }
                    }
                }
            });
        }
    }

    public ApiResponse delete(CommonIdDto idDto) {
        LambdaUpdateWrapper<TArticle> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TArticle::getId, idDto.getId());
        updateWrapper.set(TArticle::getIsDelete, 1);
        updateWrapper.set(TArticle::getStatus, 5);

        articleService.update(updateWrapper);
        return ApiResponse.newSuccess();
    }

    public ApiResponse publish(CommonIdDto idDto) {
        LambdaUpdateWrapper<TArticle> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TArticle::getId, idDto.getId());
        updateWrapper.set(TArticle::getStatus, 4);

        articleService.update(updateWrapper);
        return ApiResponse.newSuccess();
    }
}
