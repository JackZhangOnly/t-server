package com.tstartup.tserver.util;

import com.alibaba.fastjson2.JSON;
import com.google.common.base.Strings;
import com.tstartup.tserver.common.ApiToken;
import com.tstartup.tserver.common.UserApiRequest;
import com.tstartup.tserver.common.response.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.RequestWrapper;
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
import java.util.Set;

public class HttpServletUtil {

    /**
     * 用户请求信息
     */
    private static final ThreadLocal<UserApiRequest> USER_API_REQUEST_INFO = new ThreadLocal<>();

    /**
     * 语言环境
     */
    private static final Set<String> LANGUAGE_SET = Set.of("ch", "en", "ar", "ID", "zh","hi", "fr", "ur", "bn", "vi", "fil","th");
    /**
     * 用户信息
     */
    private static final ThreadLocal<ApiToken> USER_INFO = new ThreadLocal<>();

    private static final String API_TOKEN = "token";
    private static final String DEVICE_ID = "deviceId";
    private static final String DEVICE_TYPE = "deviceType";
    private static final String DEVICE_BRAND = "deviceBrand";

    private static final String DEVICE_MODEL = "deviceModel";
    private static final String API_VERSION = "apiVersion";
    private static final String APP_OS_VERSION = "appOsVersion";
    private static final String APP_VERSION_CODE = "appVersionCode";
    private static final String APP_PACKAGE = "appPackage";

    private static final String ADV_RESOURCE = "advResource";

    private static final String CHANNEL = "channel";
    private static final String APP_VERSION_NAME = "appVersionName";

    private static final String LANGUAGE = "language";

    private static final String UID = "uid";

    private static final String HOST = "host";



    /**
     * 后台登录token
     */
    private static final String CRM_TOKEN = "X-Auth-Token";


    public static UserApiRequest resolveAndSaveAttributeApiHead(HttpServletRequest request) {
        String apiToken = request.getHeader(API_TOKEN);
        String deviceId = request.getHeader(DEVICE_ID);
        String deviceType = request.getHeader(DEVICE_TYPE);
        String deviceBrand = request.getHeader(DEVICE_BRAND);
        String deviceModel = request.getHeader(DEVICE_MODEL);
        String apiVersion = request.getHeader(API_VERSION);
        String appOsVersion = request.getHeader(APP_OS_VERSION);
        String appVersionCode = request.getHeader(APP_VERSION_CODE);
        String appPackage = request.getHeader(APP_PACKAGE);
        String channel = request.getHeader(CHANNEL);
        String appVersionName = request.getHeader(APP_VERSION_NAME);
        String advResource = request.getHeader(ADV_RESOURCE);
        String language = request.getHeader(LANGUAGE);
        String crmToken = request.getHeader(CRM_TOKEN);
        String uid = request.getHeader(UID);
        String host = request.getHeader(HOST);


        apiToken = StringUtils.isBlank(apiToken) ? "" : apiToken;

        UserApiRequest userApiRequest = new UserApiRequest();
        userApiRequest.setDeviceId(deviceId);
        userApiRequest.setDeviceType(deviceType);
        userApiRequest.setDeviceBrand(deviceBrand);
        userApiRequest.setDeviceModel(deviceModel);
        userApiRequest.setApiVersion(StringUtils.isEmpty(apiVersion) ? 0 : Integer.parseInt(apiVersion));
        userApiRequest.setAppOsVersion(appOsVersion);
        userApiRequest.setAppVersionCode(appVersionCode);
        userApiRequest.setAppVersionName(appVersionName);
        userApiRequest.setAppPackage(appPackage);
        userApiRequest.setChannel(channel);
        //userApiRequest.setUserIp(HttpIpUtil.getIpAddr(request));
        userApiRequest.setAdvResource(advResource);
        userApiRequest.setLanguage(language);
        userApiRequest.setApiToken(apiToken);
        userApiRequest.setCrmToken(crmToken);
        userApiRequest.setUid(uid);
        userApiRequest.setHost(host);

        USER_API_REQUEST_INFO.set(userApiRequest);


        return userApiRequest;
    }

    /**
     * 解析headers内容
     * @param request
     * @return
     */
    public static UserApiRequest resolveAndSaveAttributeApiHead(ServerHttpRequest request) {
        final String EMPTY = "";
        HttpHeaders headers = request.getHeaders();
        String apiToken = CollectionUtils.isEmpty(headers.get(API_TOKEN)) ? EMPTY : headers.getFirst(API_TOKEN);
        String deviceId = CollectionUtils.isEmpty(headers.get(DEVICE_ID)) ? EMPTY : headers.getFirst(DEVICE_ID);
        String deviceType = CollectionUtils.isEmpty(headers.get(DEVICE_TYPE)) ? EMPTY : headers.getFirst(DEVICE_TYPE);
        String deviceBrand = CollectionUtils.isEmpty(headers.get(DEVICE_BRAND)) ? EMPTY : headers.getFirst(DEVICE_BRAND);
        String deviceModel = CollectionUtils.isEmpty(headers.get(DEVICE_MODEL)) ? EMPTY : headers.getFirst(DEVICE_MODEL);
        String apiVersion = CollectionUtils.isEmpty(headers.get(API_VERSION)) ? EMPTY : headers.getFirst(API_VERSION);
        String appOsVersion = CollectionUtils.isEmpty(headers.get(APP_OS_VERSION)) ? EMPTY : headers.getFirst(APP_OS_VERSION);
        String appVersionCode = CollectionUtils.isEmpty(headers.get(APP_VERSION_CODE)) ? EMPTY : headers.getFirst(APP_VERSION_CODE);
        String appPackage = CollectionUtils.isEmpty(headers.get(APP_PACKAGE)) ? EMPTY : headers.getFirst(APP_PACKAGE);
        String channel = CollectionUtils.isEmpty(headers.get(CHANNEL)) ? EMPTY : headers.getFirst(CHANNEL);
        String appVersionName = CollectionUtils.isEmpty(headers.get(APP_VERSION_NAME)) ? EMPTY : headers.getFirst(APP_VERSION_NAME);
        String advResource = CollectionUtils.isEmpty(headers.get(ADV_RESOURCE)) ? EMPTY : headers.getFirst(ADV_RESOURCE);
        String language = CollectionUtils.isEmpty(headers.get(LANGUAGE)) ? EMPTY : headers.getFirst(LANGUAGE);
        String uid = CollectionUtils.isEmpty(headers.get(UID)) ? "0" : headers.getFirst(UID);
        apiToken = StringUtils.isBlank(apiToken) ? "" : apiToken;

        UserApiRequest userApiRequest = new UserApiRequest();
        userApiRequest.setDeviceId(deviceId);
        userApiRequest.setDeviceType(deviceType);
        userApiRequest.setDeviceBrand(deviceBrand);
        userApiRequest.setDeviceModel(deviceModel);
        userApiRequest.setApiVersion(StringUtils.isEmpty(apiVersion) ? 0 : Integer.parseInt(apiVersion));
        userApiRequest.setAppOsVersion(appOsVersion);
        userApiRequest.setAppVersionCode(appVersionCode);
        userApiRequest.setAppVersionName(appVersionName);
        userApiRequest.setAppPackage(appPackage);
        userApiRequest.setChannel(channel);
        //userApiRequest.setUserIp(HttpIpUtil.getIpAddr(request));
        userApiRequest.setAdvResource(advResource);
        userApiRequest.setLanguage(language);
        userApiRequest.setApiToken(apiToken);
        userApiRequest.setUid(uid);

        USER_API_REQUEST_INFO.set(userApiRequest);


        return userApiRequest;
    }

    public static void setUserInfo(ApiToken apiToken) {
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
        ApiToken apiToken = USER_INFO.get();
        if (Objects.isNull(apiToken)) {
            return Optional.empty();
        }
        if (Objects.isNull(apiToken.getUserId())) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(apiToken.getUserId()));
    }




    public static void putLanguage(String language) {

        UserApiRequest userApiRequest = new UserApiRequest();
        userApiRequest.setLanguage(language);
        USER_API_REQUEST_INFO.set(userApiRequest);
    }

    /**
     * 获取语言环境
     *
     * @return 语言 en, ar, cn, in
     */
    public static String getLanguage() {
        if (Objects.isNull(USER_API_REQUEST_INFO.get())) {
            return "en";
        }
        String language = USER_API_REQUEST_INFO.get().getLanguage();
        if (StringUtils.isBlank(language)) {
            language = "en";
        }
        return LANGUAGE_SET.contains(language) ? language : "en";
    }

    public static String getAppPackage() {
        UserApiRequest userApiRequest = USER_API_REQUEST_INFO.get();
        if(Objects.nonNull(userApiRequest)) {
            return userApiRequest.getAppPackage();
        }
        return "";
    }

    public static void putAppPackage(String appPackage) {
        UserApiRequest userApiRequest = USER_API_REQUEST_INFO.get();
        if (userApiRequest == null) {
            userApiRequest = new UserApiRequest();
        }
        userApiRequest.setAppPackage(appPackage);
        USER_API_REQUEST_INFO.set(userApiRequest);
    }



    public static UserApiRequest getUserApiRequest() {
        return USER_API_REQUEST_INFO.get();
    }

    public static String getUserIp() {
        return USER_API_REQUEST_INFO.get().getUserIp();
    }


    public static void falseResponse(HttpServletResponse response, String msg, int code) throws IOException {
        response.setStatus(HttpURLConnection.HTTP_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));

        ApiResult<String> result = ApiResult.newError(code, msg);

        response.getWriter().write(JSON.toJSONString(result));
        response.getWriter().flush();
    }

    public static String getDeviceId() {
        UserApiRequest userApiRequest = USER_API_REQUEST_INFO.get();
        if (Objects.nonNull(userApiRequest)) {
            return userApiRequest.getDeviceId();
        }
        return "";
    }

}