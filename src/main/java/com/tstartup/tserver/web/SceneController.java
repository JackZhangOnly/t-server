package com.tstartup.tserver.web;


import com.tstartup.tserver.common.response.ApiResponse;
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
@Tag(name = "景点")
@RestController
@RequestMapping("/scene")
public class SceneController {

    @Resource
    private SceneBusService sceneBusService;

    @Operation(summary = "创建或更新", description = "update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ApiResponse update(HttpServletRequest request, @RequestBody @Valid SceneUpdateDto updateDto) {
        return sceneBusService.update(updateDto);
    }


    @Operation(summary = "删除", description = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ApiResponse delete(HttpServletRequest request, @RequestBody @Valid CommonIdDto commonIdDto) {
        return sceneBusService.delete(commonIdDto);
    }

    @Operation(summary = "列表查询", description = "list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ApiResponse<List<SceneItemDto>> list(HttpServletRequest request, @RequestBody @Valid SceneQryDto sceneQryDto) {
        return sceneBusService.list(sceneQryDto);
    }


}

