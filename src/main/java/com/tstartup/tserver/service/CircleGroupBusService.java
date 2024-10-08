package com.tstartup.tserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.dataobject.TCircleGroup;
import com.tstartup.tserver.persistence.service.TCircleGroupService;
import com.tstartup.tserver.web.dto.circle.CircleGroupDto;
import com.tstartup.tserver.web.dto.circle.CircleQryDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CircleGroupBusService {

    @Resource
    private TCircleGroupService circleGroupService;

    public ApiResponse<List<CircleGroupDto>> groupList(CircleQryDto qryDto) {
        Integer type = qryDto.getType();

        List<TCircleGroup> circleGroupList = circleGroupService.list(Wrappers.<TCircleGroup>lambdaQuery().eq(Objects.nonNull(type) && type == 1, TCircleGroup::getIsHot , 1));

        List<CircleGroupDto> circleGroupDtoList = circleGroupList.stream().map(tCircleGroup -> {
            CircleGroupDto circleGroupDto = new CircleGroupDto();
            BeanUtils.copyProperties(tCircleGroup, circleGroupDto);

            return circleGroupDto;
        }).collect(Collectors.toList());

        return ApiResponse.newSuccess(circleGroupDtoList);
    }
}
