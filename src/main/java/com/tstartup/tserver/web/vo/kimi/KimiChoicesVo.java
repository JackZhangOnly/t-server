/**
  * Copyright 2024 jb51.net 
  */
package com.tstartup.tserver.web.vo.kimi;
/**
 * Auto-generated: 2024-12-29 17:42:27
 *
 * @author jb51.net (i@jb51.net)
 * @website http://tools.jb51.net/code/json2javabean
 */
public class KimiChoicesVo {

    private int index;
    private KimiMessageVo message;
    private String finishReason;
    public void setIndex(int index) {
         this.index = index;
     }
     public int getIndex() {
         return index;
     }

    public void setMessage(KimiMessageVo message) {
         this.message = message;
     }
     public KimiMessageVo getMessage() {
         return message;
     }

    public void setFinishReason(String finishReason) {
         this.finishReason = finishReason;
     }
     public String getFinishReason() {
         return finishReason;
     }

}