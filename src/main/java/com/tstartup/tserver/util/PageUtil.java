package com.tstartup.tserver.util;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tstartup.tserver.common.PageVo;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageUtil {

    public static <T> PageVo<T> getPageVo(Page<T> iPage) {
        PageVo<T> page = new PageVo<T>();
        page.setTotal(iPage.getTotal());
        page.setRecords(iPage.getRecords());
        return page;
    }

    public static <T, M> PageVo<T> getPageVo(Page<M> iPage, Function<M, T> convertor) {
        PageVo<T> page = new PageVo<T>();
        page.setTotal(iPage.getTotal());
        List<M> records = iPage.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            List<T> collect = records.stream().map(convertor).collect(Collectors.toList());
            page.setRecords(collect);
        } else {
            page.setRecords(Collections.EMPTY_LIST);
        }
        return page;
    }
}