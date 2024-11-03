package com.tstartup.tserver.web;

import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.config.IgnoreLogin;
import com.tstartup.tserver.service.CityBusService;
import com.tstartup.tserver.web.dto.CityItemDto;
import com.tstartup.tserver.web.dto.CityItemOpDto;
import com.tstartup.tserver.web.dto.CityQryDto;
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
 * @ClassName CityController
 * @Description
 * @Author zhang
 * @Date 2024/8/17 22:13
 * @Version 1.0
 */
@Tag(name = "C端-城市")
@RestController
@RequestMapping("/api/t/city")
public class CityController {

    @Resource
    private CityBusService cityBusService;

    @Operation(summary = "列表", description = "list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @IgnoreLogin
    public ApiResponse<List<CityItemDto>> list(HttpServletRequest request, @RequestBody @Valid CityQryDto cityQryDto) {
        return cityBusService.list(cityQryDto);
    }


}
