package com.tstartup.tserver.web.dto.article;

import com.tstartup.tserver.web.vo.SourceItemVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ArticleItemSimpleDto {

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "headline")
    private String headline;

    @Schema(description = "0:draft 1:created 2.reviewed 3.scheduled 4.published 5:deleted")
    private Integer status;

    @Schema(description = "isHot  1:hot")
    private Integer isHot;

    @Schema(description = "source")
    private SourceItemVo source;
}
