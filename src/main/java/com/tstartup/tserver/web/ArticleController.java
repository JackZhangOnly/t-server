package com.tstartup.tserver.web;


import com.tstartup.tserver.common.PageVo;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.service.ArticleBusService;
import com.tstartup.tserver.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Administrator
 * @since 2024-08-17
 */
@Tag(name = "C端-文章")
@RestController
@RequestMapping("/api/t/article")
public class ArticleController {

    @Resource
    private ArticleBusService articleBusService;


    @Operation(summary = "列表", description = "list")
    @RequestMapping(value = "/homeList", method = RequestMethod.POST)
    public ApiResponse<PageVo<ArticleItemDto>> list(HttpServletRequest request, @RequestBody @Valid ArticleHomePageQryDto pageQryDto) {
        return articleBusService.homeList(pageQryDto);
    }

}

