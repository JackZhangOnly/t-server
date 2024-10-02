package com.tstartup.tserver.web.crm;

import com.tstartup.tserver.common.response.ApiResult;
import com.tstartup.tserver.service.crm.CrmUserBusService;
import com.tstartup.tserver.web.dto.crm.CrmUserLoginDto;
import com.tstartup.tserver.web.dto.crm.CrmUserLoginRetDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @ClassName AdminUserController
 * @Description
 * @Author zhang
 * @Date 2024/9/17 10:02
 * @Version 1.0
 */
@Tag(name = "后台-用户管理")
@RestController
@RequestMapping("/crm")
public class CrmUserController {

    @Resource
    private CrmUserBusService crmUserBusService;

    // 登录
    @Operation(summary = "登录")
    @PostMapping("/login")
    public ApiResult<CrmUserLoginRetDto> login(@RequestBody @Valid CrmUserLoginDto dto) {
        return crmUserBusService.login(dto);
    }
}
