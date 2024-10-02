package com.tstartup.tserver.enums;

/**
 * @ClassName EndPkTypeEnum
 * @Description
 * @Author zhang
 * @Date 2023/1/16 14:48
 * @Version 1.0
 */
public enum ArticleTypeEnum {

    TRIP_TYPE("tripType"),
    TAG("tag"),
    CITY("city"),
    SCENE("scene"),
    KEYWORD("keyword"),
    ARTICLE_TYPE("articleType"),
    TRANSPORT_TYPE("transportType")
    ;

    private final String type;


    ArticleTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
