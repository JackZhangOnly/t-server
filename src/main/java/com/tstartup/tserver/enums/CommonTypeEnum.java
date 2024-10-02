package com.tstartup.tserver.enums;

/**
 * @ClassName CommonTypeEnum
 * @Description
 * @Author zhang
 * @Date 2023/1/16 14:48
 * @Version 1.0
 */
public enum CommonTypeEnum {

    TRIP_TYPE("tripType"),
    TAG("tag"),
    CITY("city"),
    SCENE("sceneType"),
    SCENE_ID("sceneID"),
    KEYWORD("keyword"),
    ARTICLE_TYPE("articleType"),
    TRANSPORT_TYPE("transportType")
    ;

    private final String type;


    CommonTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
