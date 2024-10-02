package com.tstartup.tserver.web.dto.circle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CircleQryDto {

    @Schema(description = "1:热门 2.感兴趣 3.我的组 4.最近访问")
    private Integer type;
}
