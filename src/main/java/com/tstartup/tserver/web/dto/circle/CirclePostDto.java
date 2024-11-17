package com.tstartup.tserver.web.dto.circle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CirclePostDto {

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "发布uid")
    private Integer uid;

    @Schema(description = "发布作者昵称")
    private String author;

    @Schema(description = "圈子id")
    private Integer circleId;

    @Schema(description = "发布内容")
    private String content;

    @Schema(description = "创建时间")
    private Integer createTime;
}
