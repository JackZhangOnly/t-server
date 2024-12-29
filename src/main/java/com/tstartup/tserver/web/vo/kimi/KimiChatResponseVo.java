/**
  * Copyright 2024 jb51.net 
  */
package com.tstartup.tserver.web.vo.kimi;
import java.util.List;

/**
 * Auto-generated: 2024-12-29 17:42:27
 *
 * @author jb51.net (i@jb51.net)
 * @website http://tools.jb51.net/code/json2javabean
 */
public class KimiChatResponseVo {

    private String id;
    private String object;
    private int created;
    private String model;
    private List<KimiChoicesVo> choices;
    private Usage usage;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setObject(String object) {
         this.object = object;
     }
     public String getObject() {
         return object;
     }

    public void setCreated(int created) {
         this.created = created;
     }
     public int getCreated() {
         return created;
     }

    public void setModel(String model) {
         this.model = model;
     }
     public String getModel() {
         return model;
     }

    public void setChoices(List<KimiChoicesVo> choices) {
         this.choices = choices;
     }
     public List<KimiChoicesVo> getChoices() {
         return choices;
     }

    public void setUsage(Usage usage) {
         this.usage = usage;
     }
     public Usage getUsage() {
         return usage;
     }

}