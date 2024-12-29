package com.tstartup.tserver.web;

import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.config.IgnoreLogin;
import com.tstartup.tserver.service.ToolBusService;
import com.tstartup.tserver.service.crm.ImgSelectorBusService;
import com.tstartup.tserver.util.RegistrationCodeGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/api/t/tool")
public class ToolController {

    @Resource
    private ImgSelectorBusService imgSelectorBusService;

    @Resource
    private ToolBusService toolBusService;

    @RequestMapping(value = "/generateCode", method = RequestMethod.GET)
    @IgnoreLogin
    public ApiResponse<String> generateCode(HttpServletRequest request, @RequestParam("deviceId") String deviceId, @RequestParam("pwd") String pwd) {
        return imgSelectorBusService.generateCode(deviceId, pwd);
    }


    @RequestMapping(value = "/uploadArticle", method = RequestMethod.GET)
    @IgnoreLogin
    public ApiResponse<String> uploadArticle(HttpServletRequest request) {
        return toolBusService.uploadArticle();
    }

}
