package com.tstartup.tserver.web;


import com.tstartup.tserver.common.PageVo;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.config.IgnoreLogin;
import com.tstartup.tserver.service.ArticleBusService;
import com.tstartup.tserver.web.dto.*;
import com.tstartup.tserver.web.dto.article.ArticleItemSimpleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
    @IgnoreLogin
    public ApiResponse<PageVo<ArticleItemSimpleDto>> list(HttpServletRequest request, @RequestBody @Valid ArticleHomePageQryDto pageQryDto) {
        return articleBusService.homeList(pageQryDto);
    }


    @Operation(summary = "详情", description = "detail")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse<ArticleItemDto> detail(HttpServletRequest request, @RequestBody @Valid CommonIdDto commonIdDto) {
        return articleBusService.detail(commonIdDto);
    }

    @Operation(summary = "根据景点id查询文章", description = "getListBySceneId")
    @RequestMapping(value = "/getListBySceneId", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse<List<ArticleItemDto>> getListBySceneId(HttpServletRequest request, @RequestBody @Valid CommonIdDto commonIdDto) {
        return articleBusService.getListBySceneId(commonIdDto);
    }
}

