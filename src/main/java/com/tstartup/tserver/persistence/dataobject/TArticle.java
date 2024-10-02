package com.tstartup.tserver.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Administrator
 * @since 2024-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TArticle implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String headline;

    /**
     * 0:draft
     * 1:created
     * 2.reviewed
     * 3.scheduled
     * 4.published
     * 5:deleted
     */
    private Integer status;

    private String source;

    private Integer isHot;

    private Integer likes;

    private Integer tripType;

    private String startCity;

    private String destCity;

    private String hotelInfo;

    private String transportType;

    private String duration;

    private String weather;

    private String highlights;

    private String tripTime;

    private Integer isDelete;

    private Integer createTime;

    private Integer updateTime;


}
