package com.tstartup.tserver.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName ArticlePageQryDto
 * @Description
 * @Author zhang
 * @Date 2024/8/17 20:54
 * @Version 1.0
 */
@Data
public class ArticleHomePageQryDto {

    @Schema(description = "cityId")
    private Integer cityId;


    @Schema(description = "tripTypeId")
    private Integer tripTypeId;

    @Schema(description = "articleTypeId")
    private Integer articleTypeId;


    @Schema(description = "isHot  1:hot 0：not hot")
    private Integer isHot = 1;

    @Schema(description = "pageNo")
    //@NotNull(message = "pageNo is required")
    private Integer pageNo;

    @Schema(description = "pageSize")
   // @NotNull(message = "pageSize is required")
    private Integer pageSize;
}
