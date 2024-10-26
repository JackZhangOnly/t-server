package com.tstartup.tserver.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserInfoDto {

    @Schema(description = "用户id")
    private Integer id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮件")
    private String email;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "个人主页链接")
    private String pageLink;


    @Schema(description = "临时apiToken-需放入header中")
    private String apiToken;

    @Schema(description = "注册时间")
    private Integer createTime;
}
