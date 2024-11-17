package com.tstartup.tserver.config.interceptor;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tstartup.tserver.common.ApiAuthToken;
import com.tstartup.tserver.config.IgnoreLogin;
import com.tstartup.tserver.persistence.dataobject.TCrmUser;
import com.tstartup.tserver.persistence.dataobject.TUser;
import com.tstartup.tserver.persistence.service.TCrmUserService;
import com.tstartup.tserver.persistence.service.TUserService;
import com.tstartup.tserver.util.AESUtils;
import com.tstartup.tserver.util.HttpServletUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Objects;

@Component
public class ApiInterceptor implements HandlerInterceptor {

    @Resource
    private TUserService tUserService;

    /**
     * 登录token
     */
    private static final String LOGIN_TOKEN_KEY = "apiToken";

    /**
     * 进入的时间
     */
    private static final String CURRENT_TIME_MILLIS = "currentTimeMillis";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        IgnoreLogin ignoreLogin = method.getMethod().getAnnotation(IgnoreLogin.class);

        //需要校验登陆
        boolean needLogin = ignoreLogin == null || !ignoreLogin.value();

        if (needLogin) {
            String token = request.getHeader(LOGIN_TOKEN_KEY);
            String id = verifyClientUser(token);
            if (Objects.isNull(id)) {
                HttpServletUtil.falseResponse(response, "需要重新登陆", HttpURLConnection.HTTP_FORBIDDEN);
                return false;
            }

            ApiAuthToken apiToken = new ApiAuthToken();
            apiToken.setUserId(id);
            HttpServletUtil.setUserInfo(apiToken);
        }
        HttpServletUtil.parseApiHead(request);

        //进入的时间
        request.setAttribute(CURRENT_TIME_MILLIS, System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) {

    }

    private String verifyClientUser(String token) {
        TUser tUser = tUserService.getOne(Wrappers.<TUser>lambdaQuery().eq(TUser::getApiToken, token));
        if (Objects.isNull(tUser)) {
            return null;
        }
        String uid = AESUtils.decrypt(token);
        return uid;
    }

}