package com.tstartup.tserver.config.interceptor;


import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tstartup.tserver.common.ApiToken;
import com.tstartup.tserver.common.response.ApiResult;
import com.tstartup.tserver.config.IgnoreAuth;
import com.tstartup.tserver.persistence.dataobject.TCrmUser;
import com.tstartup.tserver.persistence.service.TCrmUserService;
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
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class CrmInterceptor implements HandlerInterceptor {

    @Resource
    private TCrmUserService crmUserService;

    /**
     * 后台登录token
     */
    private static final String LOGIN_TOKEN_KEY = "crm-token";

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

        IgnoreAuth ignoreAuth = method.getMethod().getAnnotation(IgnoreAuth.class);

        //需要校验登陆
        boolean needLogin = ignoreAuth == null || !ignoreAuth.value();

        if (needLogin) {
            String token = request.getHeader(LOGIN_TOKEN_KEY);
            String id = verify(token);
            if (Objects.isNull(id)) {
                HttpServletUtil.falseResponse(response, "需要重新登陆", HttpURLConnection.HTTP_FORBIDDEN);
                return false;
            }

            ApiToken apiToken = new ApiToken();
            apiToken.setUserId(id);
            HttpServletUtil.setUserInfo(apiToken);
        }
        HttpServletUtil.resolveAndSaveAttributeApiHead(request);

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

    private String verify(String token) {
        TCrmUser crmUser = crmUserService.getOne(Wrappers.<TCrmUser>lambdaQuery().eq(TCrmUser::getToken, token));
        if (Objects.isNull(crmUser)) {
            return null;
        }
        String uid = AESUtils.decrypt(token);
        return uid;
    }

}