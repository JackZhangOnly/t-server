package com.tstartup.tserver.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName SceneUpdateDto
 * @Description
 * @Author zhang
 * @Date 2024/8/18 19:31
 * @Version 1.0
 */
@Data
public class SceneUpdateDto {

    @Schema(description = "id，null表示新增")
    private Integer id;

    @Schema(description = "英文名")
    @NotNull(message = "英文名 is required")
    private String enName;

    @Schema(description = "中文名")
    private String cnName;

    @Schema(description = "描述")
    @NotNull(message = "desc is required")
    private String desc;

  /*  @Schema(description = "景点类型")
    private String sceneType;*/

    @Schema(description = "图片地址")
    //@NotNull(message = "imgUrl is required")
    private String imgUrl;

    @Schema(description = "视频地址")
   // @NotNull(message = "videoUrl is required")
    private String videoUrl;


    @Schema(description = "城市id")
    @NotNull(message = "cityId is required")
    private Integer cityId;

    @Schema(description = "address")
    @NotNull(message = "address is required")
    private String address;

    /*@Schema(description = "seasonToVisit")
    private String seasonToVisit;

    @Schema(description = "monthToVisit")
    private String monthToVisit;*/

    @Schema(description = "景点类型idList")
    private List<Integer> sceneTypeIdList;

    @Schema(description = "季节列表")
    private List<String> seasonToVisitList;

    @Schema(description = "月份列表")
    private List<String> monthToVisitList;

    @Schema(description = "是否热门 1热门")
    @NotNull
    private Integer isHot;
}
