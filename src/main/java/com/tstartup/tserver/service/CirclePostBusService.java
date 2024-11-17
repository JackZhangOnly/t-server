package com.tstartup.tserver.service;

import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.service.TCirclePostService;
import com.tstartup.tserver.web.dto.circle.CirclePostDto;
import com.tstartup.tserver.web.dto.circle.PostQryDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CirclePostBusService {
    @Resource
    private TCirclePostService circlePostService;



    public ApiResponse<List<CirclePostDto>> list(PostQryDto qryDto) {

        return ApiResponse.newSuccess();
    }
}
