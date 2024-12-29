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
public class Usage {

    private int promptTokens;

    private int completionTokens;

    private int totalTokens;
    public void setPromptTokens(int promptTokens) {
         this.promptTokens = promptTokens;
     }
     public int getPromptTokens() {
         return promptTokens;
     }

    public void setCompletionTokens(int completionTokens) {
         this.completionTokens = completionTokens;
     }
     public int getCompletionTokens() {
         return completionTokens;
     }

    public void setTotalTokens(int totalTokens) {
         this.totalTokens = totalTokens;
     }
     public int getTotalTokens() {
         return totalTokens;
     }

}