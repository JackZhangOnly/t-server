package com.tstartup.tserver.web.dto;

import com.tstartup.tserver.web.vo.SceneSimpleItemDto;
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
public class ArticleItemDto {

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "headline")
    private String headline;

    @Schema(description = "0:draft 1:created 2.reviewed 3.scheduled 4.published 5:deleted")
    private Integer status;

    @Schema(description = "source")
    private SourceItemVo source;

    @Schema(description = "isHot  1:hot")
    private Integer isHot;

    @Schema(description = "keywordList")
    private List<CommonTypeItemDto> keywordList;

    @Schema(description = "tagList")
    private List<CommonTypeItemDto> tagList;

   /* @Schema(description = "cityList")
    private List<CommonTypeItemDto> cityList;*/


    @Schema(description = "articleTypeList")
    private List<CommonTypeItemDto> articleTypeList;

    @Schema(description = "tripTypeList")
    private List<CommonTypeItemDto> tripTypeList;

    @Schema(description = "景点点列sceneList")
    private List<SceneSimpleItemDto> sceneList;

    private Integer tripType;

    @Schema(description = "startCityId")
    private Integer startCity;

    @Schema(description = "目标城市id")
    private Integer destCity;

    @Schema(description = "目标国家id")
    private Integer destCountry;

    @Schema(description = "国家名称")
    private String countryName;

    @Schema(description = "城市名称")
    private String cityName;

    private String hotelInfo;

    private String transportType;

    private String duration;

    private String weather;

    private String highlights;

    private String tripTime;

    private Integer isDelete;

    private Integer createTime;
}
