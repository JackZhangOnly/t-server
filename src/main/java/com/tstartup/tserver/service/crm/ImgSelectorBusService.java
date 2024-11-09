package com.tstartup.tserver.service.crm;

import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.dataobject.TImgSelector;
import com.tstartup.tserver.persistence.service.TImgSelectorService;
import com.tstartup.tserver.util.DateUtil;
import com.tstartup.tserver.util.RegistrationCodeGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class ImgSelectorBusService {

    @Resource
    private TImgSelectorService imgSelectorService;

    public ApiResponse<String> generateCode(String deviceId, String pwd) {
        if (Objects.isNull(deviceId) || Objects.isNull(pwd) || !"imgSelector".equals(pwd)) {
            return ApiResponse.newParamError();
        }
        String code = RegistrationCodeGenerator.generateRegistrationCode(deviceId + "j");
        TImgSelector imgSelector = new TImgSelector();
        imgSelector.setDeviceId(deviceId);
        imgSelector.setRegisterCode(code);
        imgSelector.setCreateTime(DateUtil.getNowSeconds());
        imgSelectorService.save(imgSelector);
        return ApiResponse.newSuccess(code);
    }
}
