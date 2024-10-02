package com.tstartup.tserver.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName CommonIdDto
 * @Description
 * @Author zhang
 * @Date 2024/8/17 16:47
 * @Version 1.0
 */
@Data
public class CommonIdDto implements Serializable {

    @Schema(description = "id")
    @NotNull(message = "id is required")
    private Integer id;
}
