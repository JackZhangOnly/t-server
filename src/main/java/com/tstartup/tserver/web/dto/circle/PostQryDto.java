package com.tstartup.tserver.web.dto.circle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostQryDto {

    @Schema(description = "圈子id")
    @NonNull
    private Integer circleId;
}
