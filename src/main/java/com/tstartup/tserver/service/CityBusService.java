package com.tstartup.tserver.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.api.client.util.Strings;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.dataobject.TCity;
import com.tstartup.tserver.persistence.service.TCityService;
import com.tstartup.tserver.util.DateUtil;
import com.tstartup.tserver.web.dto.CityItemDto;
import com.tstartup.tserver.web.dto.CityItemOpDto;
import com.tstartup.tserver.web.dto.CityQryDto;
import com.tstartup.tserver.web.dto.CommonIdDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName CityBusService
 * @Description
 * @Author zhang
 * @Date 2024/8/18 8:33
 * @Version 1.0
 */
@Service
public class CityBusService {

    @Resource
    private TCityService cityService;

    public ApiResponse<List<CityItemDto>> list(CityQryDto cityQryDto) {
        Integer parentId = cityQryDto.getParentId();
        parentId = Objects.isNull(parentId) ? 0 : parentId;

        List<TCity> cityList = cityService.list(Wrappers.<TCity>lambdaQuery().eq(TCity::getParentId, parentId));
        List<CityItemDto> cityItemDtoList = cityList.stream().map(tCity -> {
            CityItemDto cityItemDto = new CityItemDto();
            BeanUtils.copyProperties(tCity, cityItemDto);

            String seasonToVisit = tCity.getSeasonToVisit();
            String monthToVisit = tCity.getMonthToVisit();
            if (!Strings.isNullOrEmpty(seasonToVisit)) {
                cityItemDto.setSeasonToVisitList(JSON.parseArray(seasonToVisit, String.class));
            }
            if (!Strings.isNullOrEmpty(monthToVisit)) {
                cityItemDto.setMonthToVisitList(JSON.parseArray(monthToVisit, String.class));
            }

            return cityItemDto;
        }).collect(Collectors.toList());

        return ApiResponse.newSuccess(cityItemDtoList);
    }

    public ApiResponse update(CityItemOpDto itemOpDto) {
        Integer id = itemOpDto.getId();


        if (Objects.isNull(id)) {
            TCity tCity = new TCity();
            copyCityItem(itemOpDto, tCity);

            tCity.setCreateTime(DateUtil.getNowSeconds());

            cityService.save(tCity);
            return ApiResponse.newSuccess();
        } else {
            TCity tCity = cityService.getById(id);
            if (Objects.isNull(tCity)) {
                return ApiResponse.newParamError();
            }
            copyCityItem(itemOpDto, tCity);

            tCity.setUpdateTime(DateUtil.getNowSeconds());

            cityService.updateById(tCity);
            return ApiResponse.newSuccess();
        }
    }

    public ApiResponse delete(CommonIdDto idDto) {
        Integer id = idDto.getId();
        cityService.removeById(id);

        return ApiResponse.newSuccess();
    }

    private void copyCityItem(CityItemOpDto itemOpDto, TCity tCity) {
        String name = itemOpDto.getName();
        Integer parentId = itemOpDto.getParentId();
        Integer isHot = itemOpDto.getIsHot();
        parentId = Objects.isNull(parentId) ? 0 : parentId;


        tCity.setName(name);
        tCity.setParentId(parentId);
        tCity.setIsHot(isHot);
        tCity.setDesc(itemOpDto.getDesc());
        tCity.setSeasonToVisit(CollectionUtils.isEmpty(itemOpDto.getSeasonToVisitList()) ? "" : JSON.toJSONString(itemOpDto.getSeasonToVisitList()));
        tCity.setMonthToVisit(CollectionUtils.isEmpty(itemOpDto.getMonthToVisitList()) ? "" : JSON.toJSONString(itemOpDto.getMonthToVisitList()));

    }
}
