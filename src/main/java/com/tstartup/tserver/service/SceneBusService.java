package com.tstartup.tserver.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.api.client.util.Strings;
import com.google.common.collect.Maps;
import com.tstartup.tserver.common.response.ApiResult;
import com.tstartup.tserver.enums.CommonTypeEnum;
import com.tstartup.tserver.persistence.dataobject.TCity;
import com.tstartup.tserver.persistence.dataobject.TCommonType;
import com.tstartup.tserver.persistence.dataobject.TScene;
import com.tstartup.tserver.persistence.dataobject.TSceneTypeRelation;
import com.tstartup.tserver.persistence.service.TCityService;
import com.tstartup.tserver.persistence.service.TCommonTypeService;
import com.tstartup.tserver.persistence.service.TSceneService;
import com.tstartup.tserver.persistence.service.TSceneTypeRelationService;
import com.tstartup.tserver.util.DateUtil;
import com.tstartup.tserver.web.dto.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName SceneBusService
 * @Description
 * @Author zhang
 * @Date 2024/8/18 18:02
 * @Version 1.0
 */
@Service
public class SceneBusService {

    @Resource
    private TSceneService sceneService;

    @Resource
    private TCityService cityService;

    @Resource
    private TSceneTypeRelationService sceneTypeRelationService;

    @Resource
    private TCommonTypeService commonTypeService;

    public ApiResult update(SceneUpdateDto updateDto) {
        Integer id = updateDto.getId();
        TScene scene = null;
        if (Objects.isNull(id)) {
            scene = new TScene();
            BeanUtils.copyProperties(updateDto, scene);
            scene.setDescription(updateDto.getDesc());
            scene.setCreateTime(DateUtil.getNowSeconds());
            scene.setEnName(updateDto.getEnName());
            scene.setCnName(updateDto.getCnName());
            scene.setSeasonToVisit(CollectionUtils.isEmpty(updateDto.getSeasonToVisitList()) ? "" : JSON.toJSONString(updateDto.getSeasonToVisitList()));
            scene.setMonthToVisit(CollectionUtils.isEmpty(updateDto.getMonthToVisitList()) ? "" : JSON.toJSONString(updateDto.getMonthToVisitList()));
            sceneService.save(scene);
        } else {
            scene = sceneService.getById(id);
            if (Objects.isNull(scene)) {
                return ApiResult.newParamError();
            }
            BeanUtils.copyProperties(updateDto, scene);
            scene.setId(id);
            scene.setDescription(updateDto.getDesc());
            scene.setUpdateTime(DateUtil.getNowSeconds());
            scene.setEnName(updateDto.getEnName());
            scene.setCnName(updateDto.getCnName());
            scene.setSeasonToVisit(CollectionUtils.isEmpty(updateDto.getSeasonToVisitList()) ? "" : JSON.toJSONString(updateDto.getSeasonToVisitList()));
            scene.setMonthToVisit(CollectionUtils.isEmpty(updateDto.getMonthToVisitList()) ? "" : JSON.toJSONString(updateDto.getMonthToVisitList()));

            sceneService.updateById(scene);
        }
        Integer sceneId = scene.getId();

        saveSceneType(sceneId, updateDto.getSceneTypeIdList());

        return ApiResult.newSuccess();
    }

    private void saveSceneType(Integer sceneId, List<Integer> sceneTypeIdList) {
        if (Objects.isNull(sceneId) || CollectionUtils.isEmpty(sceneTypeIdList)) {
            return;
        }
        List<TSceneTypeRelation> sceneTypeRelationList = sceneTypeRelationService.list(Wrappers.<TSceneTypeRelation>lambdaQuery()
                .eq(TSceneTypeRelation::getSceneId, sceneId));
        if (!CollectionUtils.isEmpty(sceneTypeRelationList)) {
            sceneTypeRelationList.stream().forEach(tSceneTypeRelation -> {
                sceneTypeRelationService.removeById(tSceneTypeRelation);
            });
        }
        List<TSceneTypeRelation> relationList = sceneTypeIdList.stream().map(sceneTypeId -> {
            TSceneTypeRelation typeRelation = new TSceneTypeRelation();
            typeRelation.setTypeId(sceneTypeId);
            typeRelation.setSceneId(sceneId);
            typeRelation.setTypeIdentity(CommonTypeEnum.SCENE.getType());
            typeRelation.setCreateTime(DateUtil.getNowSeconds());

            return typeRelation;
        }).collect(Collectors.toList());
        sceneTypeRelationService.saveBatch(relationList);
    }

    public ApiResult delete(CommonIdDto idDto) {
        sceneService.removeById(idDto.getId());
        return ApiResult.newSuccess();
    }

    public ApiResult<List<SceneItemDto>> list(SceneQryDto sceneQryDto) {
        Integer cityId = sceneQryDto.getCityId();
        String sceneName = sceneQryDto.getSceneName();

        List<TScene> sceneList = sceneService.list(Wrappers.<TScene>lambdaQuery()
                .eq(Objects.nonNull(cityId) && cityId > 0, TScene::getCityId, cityId)
                .like(Objects.nonNull(sceneName), TScene::getEnName, sceneName));
        List<Integer> cityIdList = sceneList.stream().map(TScene::getCityId).collect(Collectors.toList());
        Map<Integer,TCity> cityMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(cityIdList)) {
            List<TCity> cityList = cityService.list(Wrappers.<TCity>lambdaQuery().in(TCity::getId, cityIdList));
            cityMap = cityList.stream().collect(Collectors.toMap(TCity::getId, Function.identity(), (k1, k2) -> k1));
        }
        Map<Integer, TCity> finalCityMap = cityMap;
        List<SceneItemDto> sceneItemDtoList = sceneList.stream().map(tScene -> {
            SceneItemDto sceneItemDto = new SceneItemDto();
            BeanUtils.copyProperties(tScene, sceneItemDto);

            sceneItemDto.setDesc(tScene.getDescription());

            Integer thisCityId = tScene.getCityId();
            TCity tCity = finalCityMap.get(thisCityId);
            if (Objects.nonNull(tCity)) {
                CommonTypeItemDto typeItemDto = new CommonTypeItemDto();
                BeanUtils.copyProperties(tCity, typeItemDto);

                sceneItemDto.setCity(typeItemDto);
            }
            String seasonToVisit = tScene.getSeasonToVisit();
            String monthToVisit = tScene.getMonthToVisit();
            if (!Strings.isNullOrEmpty(seasonToVisit)) {
                sceneItemDto.setSeasonToVisitList(JSON.parseArray(seasonToVisit, String.class));
            }
            if (!Strings.isNullOrEmpty(monthToVisit)) {
                sceneItemDto.setMonthToVisitList(JSON.parseArray(monthToVisit, String.class));
            }

            return sceneItemDto;
        }).collect(Collectors.toList());

        List<Integer> sceneIdList = sceneItemDtoList.stream().map(SceneItemDto::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(sceneIdList)) {
            List<TCommonType> commonTypeList = commonTypeService.list(Wrappers.<TCommonType>lambdaQuery().eq(TCommonType::getTypeIdentity, CommonTypeEnum.SCENE.getType()));
            Map<Integer, TCommonType> typeIdMap = commonTypeList.stream().collect(Collectors.toMap(TCommonType::getId, Function.identity(), (k1, k2) -> k1));

            List<TSceneTypeRelation> relationList = sceneTypeRelationService.list(Wrappers.<TSceneTypeRelation>lambdaQuery().in(TSceneTypeRelation::getSceneId, sceneIdList));
            Map<Integer, List<TSceneTypeRelation>> sceneIdRelationMap = relationList.stream().collect(Collectors.groupingBy(TSceneTypeRelation::getSceneId));

            sceneItemDtoList.stream().forEach(sceneItemDto -> {
                Integer sceneId = sceneItemDto.getId();
                List<TSceneTypeRelation> sceneTypeRelationList = sceneIdRelationMap.get(sceneId);
                if (!CollectionUtils.isEmpty(sceneTypeRelationList)) {
                    List<CommonTypeItemDto> typeItemDtoList = sceneTypeRelationList.stream().map(e -> {
                        Integer typeId = e.getTypeId();
                        TCommonType tCommonType = typeIdMap.get(typeId);
                        CommonTypeItemDto commonTypeItemDto = new CommonTypeItemDto();
                        if (Objects.nonNull(tCommonType)) {
                            BeanUtils.copyProperties(tCommonType, commonTypeItemDto);
                            commonTypeItemDto.setDesc(tCommonType.getDescription());
                        }
                        return commonTypeItemDto;
                    }).collect(Collectors.toList());


                    sceneItemDto.setSceneTypeList(typeItemDtoList);
                }
            });
        }

        return ApiResult.newSuccess(sceneItemDtoList);
    }

}
