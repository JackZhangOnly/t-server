package com.tstartup.tserver.web.dto.crm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CrmUserLoginRetDto implements Serializable {

    @Schema(description = "token")
    private String token;

    @Schema(description = "登录账号")
    @NotNull(message = "userName not null")
    private String userName;
}
