package com.tstartup.tserver.service;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tstartup.tserver.common.response.ApiResponse;
import com.tstartup.tserver.persistence.dataobject.TUser;
import com.tstartup.tserver.persistence.service.TUserService;
import com.tstartup.tserver.util.AESUtils;
import com.tstartup.tserver.util.DateUtil;
import com.tstartup.tserver.util.Md5Utils;
import com.tstartup.tserver.web.dto.user.UserInfoDto;
import com.tstartup.tserver.web.dto.user.UserLoginDto;
import com.tstartup.tserver.web.dto.user.UserRegisterDto;
import com.tstartup.tserver.web.dto.user.UserUpdateDto;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.util.Objects;

@Service
public class UserBusService {

    @Resource
    private TUserService tUserService;


    public ApiResponse register(UserRegisterDto registerDto) {
        String username = registerDto.getUsername();
        String password = registerDto.getPassword();
        String email = registerDto.getEmail();


        TUser tUser = tUserService.getOne(Wrappers.<TUser>lambdaQuery()
                .eq(TUser::getUsername,  username)
                .last("limit 1"));
        if (Objects.nonNull(tUser)) {
            return ApiResponse.newParamError("Username already exists");
        }
        tUser = tUserService.getOne(Wrappers.<TUser>lambdaQuery()
                .eq(TUser::getEmail,  email)
                .last("limit 1"));
        if (Objects.nonNull(tUser)) {
            return ApiResponse.newParamError("Email already exists");
        }


        tUser = new TUser();
        tUser.setUsername(username);
        tUser.setPassword(Md5Utils.md5Hex(password));
        tUser.setEmail(email);
        tUser.setCreateTime(DateUtil.getNowSeconds());

        tUserService.save(tUser);

        return ApiResponse.newSuccess();
    }

    public ApiResponse<UserInfoDto> login(UserLoginDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        // 构建查询条件
        QueryWrapper<TUser> queryWrapper = Wrappers.query();
        queryWrapper.eq("password", Md5Utils.md5Hex(password))
                .and(wrapper -> wrapper.eq("username", username).or().eq("email", username)).last("limit 1");

        TUser tUser = tUserService.getOne(queryWrapper);

        if (Objects.isNull(tUser)) {
            return ApiResponse.newParamError("Wrong username or password");
        }

        String token = AESUtils.encrypt(String.valueOf(tUser .getId()));
        tUser.setApiToken(token);

        LambdaUpdateWrapper<TUser> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.set(TUser::getApiToken, token);

        updateWrapper.eq(TUser::getId, tUser.getId());
        tUserService.update(updateWrapper);

        UserInfoDto userInfoDto = new UserInfoDto();

        BeanUtils.copyProperties(tUser, userInfoDto);

        return ApiResponse.newSuccess(userInfoDto);

    }

    public ApiResponse update(UserUpdateDto updateDto, Integer uid) {
        TUser tUser = tUserService.getById(uid);
        if (Objects.isNull(tUser)) {
            return ApiResponse.newParamError();
        }
        tUser.setPageLink(updateDto.getPageLink());
        tUser.setNickName(updateDto.getNickName());

        tUserService.updateById(tUser);

        return ApiResponse.newSuccess();
    }



}
