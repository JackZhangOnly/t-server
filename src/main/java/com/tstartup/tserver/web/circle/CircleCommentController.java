package com.tstartup.tserver.web.circle;

import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.config.IgnoreLogin;
import com.tstartup.tserver.service.CircleGroupBusService;
import com.tstartup.tserver.web.dto.circle.CircleGroupDto;
import com.tstartup.tserver.web.dto.circle.CircleQryDto;
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
@RequestMapping("/api/t/circleComment")
public class CircleCommentController {

    @Resource
    private CircleGroupBusService circleGroupBusService;

    @Operation(summary = "圈组列表", description = "list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse<List<CircleGroupDto>> list(HttpServletRequest request, @RequestBody @Valid CircleQryDto qryDto) {
        return circleGroupBusService.groupList(qryDto);
    }


}
