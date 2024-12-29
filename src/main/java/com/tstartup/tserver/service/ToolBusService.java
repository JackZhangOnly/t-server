package com.tstartup.tserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.service.TArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ToolBusService {

    @Resource
    private TArticleService tArticleService;

    public ApiResponse genHighlight() {
        tArticleService.list(Wrappers.lambdaQuery());


        return ApiResponse.newSuccess();
    }
}
