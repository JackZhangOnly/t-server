package com.tstartup.tserver.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName ArticlePageQryDto
 * @Description
 * @Author zhang
 * @Date 2024/8/17 20:54
 * @Version 1.0
 */
@Data
public class ArticlePageQryDto {

    @Schema(description = "countryId")
    private Integer countryId;


    @Schema(description = "articleTypeId")
    private Integer articleTypeId;


    @Schema(description = "headline标题")
    private String headline;


    @Schema(description = "pageNo")
    private Integer pageNo;

    @Schema(description = "pageSize")
    private Integer pageSize;
}
