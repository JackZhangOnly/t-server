package com.tstartup.tserver.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class UserRegisterDto {

    @Schema(description = "用户名称")
    @NotNull
    private String username;

    @Schema(description = "密码")
    @NotNull
    private String password;

    @Schema(description = "邮箱")
    @NotNull
    private String email;

}
