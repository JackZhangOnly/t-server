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
    private static final String DEVICE_ID = "deviceId";

    private static final String CHANNEL = "channel";

    private static final String LANGUAGE = "language";

    private static final String UID = "uid";

    private static final String HOST = "host";





    public static UserRequest resolveAndSaveAttributeApiHead(HttpServletRequest request) {
        String apiToken = request.getHeader(API_TOKEN);
        String deviceId = request.getHeader(DEVICE_ID);
        String channel = request.getHeader(CHANNEL);
        String language = request.getHeader(LANGUAGE);
        String uid = request.getHeader(UID);
        String host = request.getHeader(HOST);


        apiToken = StringUtils.isBlank(apiToken) ? "" : apiToken;

        UserRequest userRequest = new UserRequest();
        userRequest.setDeviceId(deviceId);
        userRequest.setChannel(channel);
        userRequest.setLanguage(language);
        userRequest.setApiToken(apiToken);
        userRequest.setUid(uid);
        userRequest.setHost(host);

        USER_REQUEST_INFO.set(userRequest);


        return userRequest;
    }

    /**
     * 解析headers内容
     * @param request
     * @return
     */
    public static UserRequest resolveAndSaveAttributeApiHead(ServerHttpRequest request) {
        final String EMPTY = "";
        HttpHeaders headers = request.getHeaders();
        String apiToken = CollectionUtils.isEmpty(headers.get(API_TOKEN)) ? EMPTY : headers.getFirst(API_TOKEN);
        String deviceId = CollectionUtils.isEmpty(headers.get(DEVICE_ID)) ? EMPTY : headers.getFirst(DEVICE_ID);
        String channel = CollectionUtils.isEmpty(headers.get(CHANNEL)) ? EMPTY : headers.getFirst(CHANNEL);

        String language = CollectionUtils.isEmpty(headers.get(LANGUAGE)) ? EMPTY : headers.getFirst(LANGUAGE);
        String uid = CollectionUtils.isEmpty(headers.get(UID)) ? "0" : headers.getFirst(UID);
        apiToken = StringUtils.isBlank(apiToken) ? "" : apiToken;

        UserRequest userApiRequest = new UserRequest();
        userApiRequest.setDeviceId(deviceId);
        userApiRequest.setChannel(channel);
        userApiRequest.setLanguage(language);
        userApiRequest.setApiToken(apiToken);
        userApiRequest.setUid(uid);

        USER_REQUEST_INFO.set(userApiRequest);


        return userApiRequest;
    }

    public static void setUserInfo(ApiAuthToken apiToken) {
        USER_INFO.set(apiToken);
    }



    /**
     * 获取当前登陆用户的uid,
     *
     * @param request
     * @return
     */
    public static Optional<Integer> getCurrentUid(HttpServletRequest request) {
        String userIdHeader = request.getHeader(UID);
        if (!Strings.isNullOrEmpty(userIdHeader)) {
            return Optional.of(Integer.parseInt(userIdHeader));
        }
        ApiAuthToken apiToken = USER_INFO.get();
        if (Objects.isNull(apiToken)) {
            return Optional.empty();
        }
        if (Objects.isNull(apiToken.getUserId())) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(apiToken.getUserId()));
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