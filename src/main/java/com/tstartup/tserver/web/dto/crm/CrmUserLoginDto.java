package com.tstartup.tserver.web.dto.crm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CrmUserLoginDto implements Serializable {
    /**
     * 登录账号
     */
    @Schema(description = "登录账号")
    @NotNull(message = "userName not null")
    private String userName;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @NotNull(message = "password not null")
    private String password;
}
