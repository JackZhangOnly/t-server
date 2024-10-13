package com.tstartup.tserver.web;


import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.service.CommonTypeBusService;
import com.tstartup.tserver.web.dto.CommonIdDto;
import com.tstartup.tserver.web.dto.CommonTypeItemDto;
import com.tstartup.tserver.web.dto.CommonTypeQryDto;
import com.tstartup.tserver.web.dto.CommonTypeUpdateDto;
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
 * @since 2024-08-17
 */
/*
@Tag(name = "类型分类管理")
@RestController
@RequestMapping("/api/t/commonType")
public class CommonTypeController {

    @Resource
    private CommonTypeBusService commonTypeBusService;


    @Operation(summary = "列表查询", description = "list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ApiResponse<List<CommonTypeItemDto>> list(HttpServletRequest request, @RequestBody @Valid CommonTypeQryDto typeQryDto) {
        return commonTypeBusService.list(typeQryDto);
    }

}
*/

