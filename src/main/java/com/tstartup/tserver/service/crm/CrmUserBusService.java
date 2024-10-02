package com.tstartup.tserver.service.crm;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.common.response.ResultCodeEnum;
import com.tstartup.tserver.persistence.dataobject.TCrmUser;
import com.tstartup.tserver.persistence.service.TCrmUserService;
import com.tstartup.tserver.util.AESUtils;
import com.tstartup.tserver.util.DateUtil;
import com.tstartup.tserver.web.dto.crm.CrmUserLoginDto;
import com.tstartup.tserver.web.dto.crm.CrmUserLoginRetDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @ClassName CrmUserService
 * @Description
 * @Author zhang
 * @Date 2024/9/17 14:13
 * @Version 1.0
 */
@Service
public class CrmUserBusService {

    @Resource
    private TCrmUserService crmUserService;

    public ApiResponse<CrmUserLoginRetDto> login(CrmUserLoginDto dto) {
        String username = dto.getUserName();
        String pwd = dto.getPassword();

        TCrmUser tCrmUser = crmUserService.getOne(Wrappers.<TCrmUser>lambdaQuery()
                .eq(TCrmUser::getUsername, username)
                .eq(TCrmUser::getPwd, DigestUtil.md5Hex(pwd))
                .eq(TCrmUser::getStatus, 1)
                .last("limit 1"));
        if (Objects.isNull(tCrmUser)) {
            return ApiResponse.newError(ResultCodeEnum.ERROR_PWD);
        }

        String token = AESUtils.encrypt(String.valueOf(tCrmUser.getId()));
        CrmUserLoginRetDto retDto = new CrmUserLoginRetDto();
        retDto.setToken(token);
        retDto.setUserName(username);

        Date expireDate = DateUtil.addDays(DateUtil.getCurrentDate(), 3);

        tCrmUser.setToken(token);
        tCrmUser.setUpdateTime(DateUtil.getNowSeconds());
        tCrmUser.setExpireTime(DateUtil.getDateSeconds(expireDate).intValue());

        crmUserService.updateById(tCrmUser);

        return ApiResponse.newSuccess(retDto);
    }
}
