package com.tstartup.tserver.web.crm;


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
@Tag(name = "后台-文章")
@RestController
@RequestMapping("/crm/article")
public class CrmArticleController {

    @Resource
    private ArticleBusService articleBusService;


    @Operation(summary = "创建或更新", description = "update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ApiResponse update(HttpServletRequest request, @RequestBody @Valid ArticleUpdateDto updateDto) {
        return articleBusService.update(updateDto);
    }


    @Operation(summary = "删除", description = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ApiResponse delete(HttpServletRequest request, @RequestBody @Valid CommonIdDto commonIdDto) {
        return articleBusService.delete(commonIdDto);
    }

    @Operation(summary = "列表-后台管理", description = "ListByAdmin")
    @RequestMapping(value = "/ListByAdmin", method = RequestMethod.POST)
    public ApiResponse<PageArticleVo> ListByAdmin(HttpServletRequest request, @RequestBody @Valid ArticlePageQryDto pageQryDto) {
        return articleBusService.listByAdmin(pageQryDto);
    }

    @Operation(summary = "发布", description = "publish")
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public ApiResponse publish(HttpServletRequest request, @RequestBody @Valid CommonIdDto commonIdDto) {
        return articleBusService.publish(commonIdDto);
    }
}

