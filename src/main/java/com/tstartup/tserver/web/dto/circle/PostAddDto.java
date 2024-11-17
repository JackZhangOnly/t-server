package com.tstartup.tserver.web.dto.circle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class PostAddDto {

    @Schema(description = "圈子id")
    @NonNull
    private Integer circleId;

    @Schema(description = "发布内容")
    @NonNull
    private String content;
}
