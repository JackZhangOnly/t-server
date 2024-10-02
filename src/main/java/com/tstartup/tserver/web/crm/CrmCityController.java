package com.tstartup.tserver.web.crm;

import com.tstartup.tserver.common.response.ApiResult;
import com.tstartup.tserver.service.CityBusService;
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
import java.util.List;

/**
 * @ClassName CityController
 * @Description
 * @Author zhang
 * @Date 2024/8/17 22:13
 * @Version 1.0
 */
@Tag(name = "后台-城市")
@RestController
@RequestMapping("/crm/city")
public class CrmCityController {

    @Resource
    private CityBusService cityBusService;

    @Operation(summary = "列表", description = "list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ApiResult<List<CityItemDto>> list(HttpServletRequest request,  @RequestBody @Valid CityQryDto cityQryDto) {
        return cityBusService.list(cityQryDto);
    }


    @Operation(summary = "更新", description = "update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ApiResult update(HttpServletRequest request,  @RequestBody @Valid CityItemOpDto itemOpDto) {
        return cityBusService.update(itemOpDto);
    }

    @Operation(summary = "delete", description = "delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ApiResult delete(HttpServletRequest request,  @RequestBody @Valid CommonIdDto idDto) {
        return cityBusService.delete(idDto);
    }
}
