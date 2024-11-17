package com.tstartup.tserver.web.dto.circle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

@Data
public class CommentQryDto {

    @Schema(description = "post id")
    @NonNull
    private Integer postId;
}
