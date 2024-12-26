package com.tstartup.tserver.web;


import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.config.IgnoreLogin;
import com.tstartup.tserver.service.SceneBusService;
import com.tstartup.tserver.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2024-08-18
 */
@Tag(name = "C端-景点")
@RestController
@RequestMapping("/api/t/scene")
public class SceneController {

    @Resource
    private SceneBusService sceneBusService;


    @Operation(summary = "列表查询", description = "list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse<List<SceneItemDto>> list(HttpServletRequest request, @RequestBody @Valid SceneQryDto sceneQryDto) {
        return sceneBusService.list(sceneQryDto);
    }

    @Operation(summary = "详情", description = "detail")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse<SceneItemDto> detail(HttpServletRequest request, @RequestBody @Valid CommonIdDto dto) {
        return sceneBusService.detail(dto);
    }

}

