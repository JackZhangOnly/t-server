package com.tstartup.tserver.web.dto.circle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

@Data
public class PostQryDto {

    @Schema(description = "圈子id")
    @NonNull
    private Integer circleId;
}
