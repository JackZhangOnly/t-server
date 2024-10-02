package com.tstartup.tserver.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName SceneItemDto
 * @Description
 * @Author zhang
 * @Date 2024/8/18 19:31
 * @Version 1.0
 */
@Data
public class SceneItemDto {

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

    @Schema(description = "图片地址")
    @NotNull(message = "imgUrl is required")
    private String imgUrl;

    @Schema(description = "视频地址")
    @NotNull(message = "videoUrl is required")
    private String videoUrl;

    @Schema(description = "景点类型信息")
    private List<CommonTypeItemDto> sceneTypeList;


    @Schema(description = "城市信息")
    @NotNull(message = "cityId is required")
    private CommonTypeItemDto city;

    @Schema(description = "address")
    @NotNull(message = "address is required")
    private String address;

    @Schema(description = "seasonToVisit")
    private List<String> seasonToVisitList;

    @Schema(description = "monthToVisit")
    private List<String> monthToVisitList;

    @Schema(description = "是否热门 1热门")
    @NotNull
    private Integer isHot;
}
