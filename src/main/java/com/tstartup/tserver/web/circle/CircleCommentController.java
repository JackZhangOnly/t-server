package com.tstartup.tserver.web.circle;

import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.config.IgnoreLogin;
import com.tstartup.tserver.service.CircleCommentBusService;
import com.tstartup.tserver.service.CircleGroupBusService;
import com.tstartup.tserver.util.HttpServletUtil;
import com.tstartup.tserver.web.dto.circle.*;
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

@Tag(name = "C端-圈子-评论")
@RestController
@RequestMapping("/api/t/circleComment")
public class CircleCommentController {

    @Resource
    private CircleCommentBusService circleCommentBusService;

    @Operation(summary = "评论列表", description = "list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse<List<CircleCommentDto>> list(HttpServletRequest request, @RequestBody @Valid CommentQryDto qryDto) {
        return circleCommentBusService.list(qryDto);
    }


    @Operation(summary = "发帖", description = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResponse add(HttpServletRequest request, @RequestBody @Valid CommentAddDto dto) throws Exception {
        Integer uid = HttpServletUtil.getCurrentUid();
        return circleCommentBusService.add(uid, dto);
    }


}
