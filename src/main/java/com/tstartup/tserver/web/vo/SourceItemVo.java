package com.tstartup.tserver.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName SourceItemVo
 * @Description
 * @Author zhang
 * @Date 2024/8/18 10:31
 * @Version 1.0
 */
@Data
public class SourceItemVo {

  /*  @Schema(description = "名称")
    private String name;*/

    @Schema(description = "链接")
    private String url;

    @Schema(description = "1纯文本 2图片 3视频")
    private String mediaType;

    @Schema(description = "多媒体链接")
    private String mediaLink;

   /* @Schema(description = "作者")
    private String author;*/

}
