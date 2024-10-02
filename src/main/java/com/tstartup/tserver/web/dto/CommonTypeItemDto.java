package com.tstartup.tserver.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName CommonTypeItemDto
 * @Description
 * @Author zhang
 * @Date 2024/8/17 16:52
 * @Version 1.0
 */
@Data
public class CommonTypeItemDto {

    @Schema(description = "id，不传为更新")
    private Integer id;

    @Schema(description = "名称")
    @NotNull(message = "name is required")
    private String name;

    @Schema(description = "描述")
    @NotNull(message = "desc is required")
    private String desc;


    @Schema(description = "是否热门 1热门")
    @NotNull
    private Integer isHot;
}
