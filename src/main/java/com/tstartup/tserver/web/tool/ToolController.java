package com.tstartup.tserver.web.tool;


import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.config.IgnoreLogin;
import com.tstartup.tserver.service.ToolBusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/t/tool")
public class ToolController {


    @Resource
    private ToolBusService toolBusService;

    @Operation(summary = "genHighlight", description = "genHighlight")
    @RequestMapping(value = "/genHighlight", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse genHighlight(HttpServletRequest request) {
        toolBusService.genHighlight();

        return ApiResponse.newSuccess();
    }

}
