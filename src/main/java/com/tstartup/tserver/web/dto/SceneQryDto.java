package com.tstartup.tserver.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName SceneQryDto
 * @Description
 * @Author zhang
 * @Date 2024/9/4 22:16
 * @Version 1.0
 */
@Data
public class SceneQryDto {

    @Schema(description = "城市id")
    private Integer cityId;

    //@Schema(description = "景点名称")
    //private String sceneName;

}
