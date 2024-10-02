package com.tstartup.tserver.common.response;

import lombok.Data;

/**
 * @ClassName ZegoResult
 * @Description
 * @Author zhang
 * @Date 2023/2/27 15:02
 * @Version 1.0
 */
@Data
public class ZegoResult<T> {

    private Integer Code;

    private String Message;

    private T data;

    private String RequestId;
}
