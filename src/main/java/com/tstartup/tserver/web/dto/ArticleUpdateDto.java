package com.tstartup.tserver.web.dto;

import com.tstartup.tserver.web.vo.SourceItemVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName ArticleUpdateDto
 * @Description
 * @Author zhang
 * @Date 2024/8/17 17:37
 * @Version 1.0
 */
@Data
public class ArticleUpdateDto {

    @Schema(description = "id，null表示新增")
    private Integer id;

    @Schema(description = "headline")
    @NotNull(message = "headline is required")
    private String headline;

    @Schema(description = "0:draft 1:created 2.reviewed 3.scheduled 4.published 5:deleted")
    @NotNull(message = "status is required")
    private Integer status;

    @Schema(description = "source来源")
    private SourceItemVo sourceVo;

    @Schema(description = "keywordIdList")
    private List<Integer> keywordIdList;


    @Schema(description = "tags idList")
    //@NotNull(message = "tagIdList is required")
    private List<Integer> tagIdList;

    @Schema(description = "city idList")
    //@NotNull(message = "cityIdList is required")
    private List<Integer> cityIdList;

    @Schema(description = "scene idList")
    private List<Integer> sceneIdList;


    @Schema(description = "articleTypeIdList")
    private List<Integer> articleTypeIdList;

    @Schema(description = "trip类型idList")
    private List<Integer> tripTypeIdList;

    @Schema(description = "startCity")
    private String startCity;

    @Schema(description = "destCity")
    private String destCity;

    @Schema(description = "hotelInfo")
    private String hotelInfo;

    @Schema(description = "transportType")
    private String transportType;

    @Schema(description = "duration")
    private String duration;

    @Schema(description = "weather")
    private String weather;

    @Schema(description = "highlights")
    private String highlights;

    @Schema(description = "tripTime")
    private String tripTime;

    @Schema(description = "是否热门 1热门")
    private Integer isHot = 0;
}
