package com.tstartup.tserver.web.user;

import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.config.IgnoreLogin;
import com.tstartup.tserver.service.UserBusService;
import com.tstartup.tserver.util.HttpServletUtil;
import com.tstartup.tserver.web.dto.user.UserInfoDto;
import com.tstartup.tserver.web.dto.user.UserLoginDto;
import com.tstartup.tserver.web.dto.user.UserRegisterDto;
import com.tstartup.tserver.web.dto.user.UserUpdateDto;
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

@Tag(name = "C端-用户相关")
@RestController
@RequestMapping("/api/t/user")
public class ClientUserController {

    @Resource
    private UserBusService userBusService;

    @Operation(summary = "注册", description = "register")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse register(HttpServletRequest request, @RequestBody @Valid UserRegisterDto registerDto) {
        return userBusService.register(registerDto);
    }

    @Operation(summary = "登录", description = "login")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse<UserInfoDto> login(HttpServletRequest request, @RequestBody @Valid UserLoginDto userLoginDto) {
        return userBusService.login(userLoginDto);
    }

    @Operation(summary = "更新个资料", description = "update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ApiResponse update(HttpServletRequest request, @RequestBody @Valid UserUpdateDto updateDto) throws Exception {
        Integer uid = HttpServletUtil.getCurrentUid();

        return userBusService.update(updateDto, uid);
    }
}
