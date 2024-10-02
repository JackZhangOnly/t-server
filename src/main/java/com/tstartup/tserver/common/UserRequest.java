package com.tstartup.tserver.common;

import java.io.Serializable;

public class UserRequest implements Serializable {

    /**
     * 设备Id
     */
    private String deviceId;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备品牌
     */
    private String deviceBrand;


    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * app 系统版本
     */
    private String appOsVersion;

    /**
     * app 包名
     */
    private String appPackage;

    /**
     * app 版本code
     */
    private String appVersionCode;

    /**
     * app 版本名称
     */
    private String appVersionName;

    /**
     * app 发布渠道
     */
    private String channel;

    /**
     * 接口版本
     */
    private int apiVersion;

    /**
     * 语言
     */
    private String language;

    /**
     * 用户ip
     */
    private String userIp;

    /**
     * 广告渠道
     */
    private String advResource;

    /**
     * 用户Token
     */
    private String apiToken;

    /**
     * crm后台用户token
     */
    private String crmToken;

    /**
     *
     * 用户uid
     */
    private String uid;


    private String host;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getAppOsVersion() {
        return appOsVersion;
    }

    public void setAppOsVersion(String appOsVersion) {
        this.appOsVersion = appOsVersion;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getAdvResource() {
        return advResource;
    }

    public void setAdvResource(String advResource) {
        this.advResource = advResource;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getCrmToken() {
        return crmToken;
    }

    public void setCrmToken(String crmToken) {
        this.crmToken = crmToken;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


}