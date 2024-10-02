package com.tstartup.tserver.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName CityOpDto
 * @Description
 * @Author zhang
 * @Date 2024/8/31 21:21
 * @Version 1.0
 */
@Data
public class CityItemOpDto {

    @Schema(description = "id, 不传表示新增")
    private Integer id;

    @Schema(description = "name")
    @NotNull
    private String name;


    @Schema(description = "是否热门 1热门")
    @NotNull
    private Integer isHot;

    @Schema(description = "父级id; 0表示无父级，为国家")
    @NotNull
    private Integer parentId;

    @Schema(description = "描述")
    private String desc;

    @Schema(description = "季节列表")
    private List<String> seasonToVisitList;

    @Schema(description = "月份列表")
    private List<String> monthToVisitList;
}
