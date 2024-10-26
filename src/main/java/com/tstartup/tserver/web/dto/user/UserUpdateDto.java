package com.tstartup.tserver.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserUpdateDto {

    @Schema(description = "昵称")
    @NotNull
    private String nickName;

    @Schema(description = "个人主页链接")
    @NotNull
    private String pageLink;
}
