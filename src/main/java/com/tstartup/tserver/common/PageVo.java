package com.tstartup.tserver.common;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageVo<T> implements Serializable {
    /**
     * 数据
     */
    @Schema(description = "数据")
    private List<T> records = Lists.newArrayList();

    /**
     * 总数
     */
    @Schema(description = "总数")
    private Long total = 0L;
}