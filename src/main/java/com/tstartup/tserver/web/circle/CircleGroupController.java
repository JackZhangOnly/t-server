package com.tstartup.tserver.web.circle;

import com.tstartup.tserver.common.PageVo;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.config.IgnoreLogin;
import com.tstartup.tserver.service.CircleGroupBusService;
import com.tstartup.tserver.util.HttpServletUtil;
import com.tstartup.tserver.web.dto.ArticleItemDto;
import com.tstartup.tserver.web.dto.ArticlePageQryDto;
import com.tstartup.tserver.web.dto.CommonIdDto;
import com.tstartup.tserver.web.dto.circle.CircleGroupDto;
import com.tstartup.tserver.web.dto.circle.CircleGroupJoinDto;
import com.tstartup.tserver.web.dto.circle.CircleQryDto;
import com.tstartup.tserver.web.dto.circle.PostAddDto;
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

@Tag(name = "C端-圈子-分组")
@RestController
@RequestMapping("/api/t/circleGroup")
public class CircleGroupController {

    @Resource
    private CircleGroupBusService circleGroupBusService;

    @Operation(summary = "圈组列表", description = "list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse<List<CircleGroupDto>> list(HttpServletRequest request, @RequestBody @Valid CircleQryDto qryDto) {
        return circleGroupBusService.groupList(qryDto);
    }

    @Operation(summary = "详情", description = "detail")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse<CircleGroupDto> detail(HttpServletRequest request, @RequestBody @Valid CommonIdDto qryDto) {
        return circleGroupBusService.detail(qryDto);
    }

    @Operation(summary = "加入圈子", description = "join")
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public ApiResponse join(HttpServletRequest request, @RequestBody @Valid CircleGroupJoinDto dto) throws Exception {
        Integer uid = HttpServletUtil.getCurrentUid();


        return circleGroupBusService.join(uid, dto);
    }

    @Operation(summary = "我的圈子", description = "myGroupList")
    @RequestMapping(value = "/myGroupList", method = RequestMethod.POST)
    public ApiResponse<List<CircleGroupDto>> myGroupList(HttpServletRequest request) throws Exception {
        Integer uid = HttpServletUtil.getCurrentUid();


        return circleGroupBusService.myGroupList(uid);
    }

}
