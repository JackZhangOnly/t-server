package com.tstartup.tserver.util;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.Strings;
import com.tstartup.tserver.common.ApiAuthToken;
import com.tstartup.tserver.common.UserRequest;
import com.tstartup.tserver.common.response.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class HttpServletUtil {

    /**
     * 用户请求信息
     */
    private static final ThreadLocal<UserRequest> USER_REQUEST_INFO = new ThreadLocal<>();

    /**
     * 用户信息
     */
    private static final ThreadLocal<ApiAuthToken> USER_INFO = new ThreadLocal<>();

    private static final String API_TOKEN = "apiToken";
    private static final String CHANNEL = "channel";

    private static final String LANGUAGE = "language";

    private static final String UID = "uid";

    private static final String HOST = "host";





    public static UserRequest parseApiHead(HttpServletRequest request) {
        String apiToken = request.getHeader(API_TOKEN);
        String channel = request.getHeader(CHANNEL);
        String language = request.getHeader(LANGUAGE);
        String uid = request.getHeader(UID);
        String host = request.getHeader(HOST);


        apiToken = StringUtils.isBlank(apiToken) ? "" : apiToken;

        UserRequest userRequest = new UserRequest();
        userRequest.setChannel(channel);
        userRequest.setLanguage(language);
        userRequest.setApiToken(apiToken);
        userRequest.setUid(uid);
        userRequest.setHost(host);

        USER_REQUEST_INFO.set(userRequest);


        return userRequest;
    }


    public static void setUserInfo(ApiAuthToken apiToken) {
        USER_INFO.set(apiToken);
    }




    public static Integer getCurrentUid() throws Exception {
        ApiAuthToken apiToken = USER_INFO.get();
        if (Objects.isNull(apiToken) || Objects.isNull(apiToken.getUserId())) {
            throw new Exception("forbidden api");
        }
        return Integer.parseInt(apiToken.getUserId());
    }

    public static void falseResponse(HttpServletResponse response, String msg, int code) throws IOException {
        response.setStatus(HttpURLConnection.HTTP_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));

        ApiResponse<String> result = ApiResponse.newError(code, msg);

        response.getWriter().write(JSON.toJSONString(result));
        response.getWriter().flush();
    }


}