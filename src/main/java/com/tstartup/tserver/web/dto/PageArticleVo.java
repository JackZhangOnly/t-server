package com.tstartup.tserver.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName PageArticleVo
 * @Description
 * @Author zhang
 * @Date 2024/9/13 7:51
 * @Version 1.0
 */
@Data
public class PageArticleVo<T> {

    /**
     * 数据
     */
    @Schema(description = "数据")
    private List<T> records;

    /**
     * 总数
     */
    @Schema(description = "总数")
    private Long total;
}
