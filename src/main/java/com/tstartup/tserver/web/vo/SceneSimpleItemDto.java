package com.tstartup.tserver.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName SceneSimpleItemDto
 * @Description
 * @Author zhang
 * @Date 2024/9/14 7:13
 * @Version 1.0
 */
@Data
public class SceneSimpleItemDto {

    @Schema(description = "id，null表示新增")
    private Integer id;

    @Schema(description = "英文名")
    @NotNull(message = "英文名 is required")
    private String enName;

    @Schema(description = "中文名")
    private String cnName;
}
