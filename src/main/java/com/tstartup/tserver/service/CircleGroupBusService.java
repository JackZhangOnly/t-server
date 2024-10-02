package com.tstartup.tserver.service;

import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.service.TCircleGroupService;
import com.tstartup.tserver.web.dto.circle.CircleGroupDto;
import com.tstartup.tserver.web.dto.circle.CircleQryDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CircleGroupBusService {

    @Resource
    private TCircleGroupService circleGroupService;

    public ApiResponse<List<CircleGroupDto>> groupList(CircleQryDto qryDto) {
        Integer type = qryDto.getType();

        return ApiResponse.newSuccess();
    }
}
