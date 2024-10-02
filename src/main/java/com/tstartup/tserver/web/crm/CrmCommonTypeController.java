package com.tstartup.tserver.web.crm;


import com.tstartup.tserver.common.response.ApiResult;
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
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2024-08-17
 */
@Tag(name = "后台-类型分类管理")
@RestController
@RequestMapping("/crm/commonType")
public class CrmCommonTypeController {

    @Resource
    private CommonTypeBusService commonTypeBusService;


    @Operation(summary = "创建或更新", description = "update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ApiResult update(HttpServletRequest request, @RequestBody @Valid CommonTypeUpdateDto updateDto) {
       return commonTypeBusService.update(updateDto);
    }


    @Operation(summary = "删除", description = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ApiResult delete(HttpServletRequest request, @RequestBody @Valid CommonIdDto commonIdDto) {
        return commonTypeBusService.delete(commonIdDto);
    }

    @Operation(summary = "列表查询", description = "list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ApiResult<List<CommonTypeItemDto>> list(HttpServletRequest request, @RequestBody @Valid CommonTypeQryDto typeQryDto) {
        return commonTypeBusService.list(typeQryDto);
    }

}

