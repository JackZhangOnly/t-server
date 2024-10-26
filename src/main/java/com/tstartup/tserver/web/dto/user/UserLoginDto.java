package com.tstartup.tserver.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserLoginDto {

    @Schema(description = "用户/邮件")
    @NotNull
    private String username;

    @Schema(description = "密码")
    @NotNull
    private String password;
}
