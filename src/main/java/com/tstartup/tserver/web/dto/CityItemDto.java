package com.tstartup.tserver.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName CityItemDto
 * @Description
 * @Author zhang
 * @Date 2024/8/18 8:43
 * @Version 1.0
 */
@Data
public class CityItemDto {

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "是否热门 1热门")
    private Integer isHot;

    @Schema(description = "父级id; 0表示无父级，为国家")
    private Integer parentId;


    @Schema(description = "描述")
    private String desc;

    @Schema(description = "季节列表")
    private List<String> seasonToVisitList;

    @Schema(description = "月份列表")
    private List<String> monthToVisitList;
}
