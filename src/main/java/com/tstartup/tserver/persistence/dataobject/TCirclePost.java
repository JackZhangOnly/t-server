package com.tstartup.tserver.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2024-10-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TCirclePost implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer uid;

    private Integer circleId;

    private String content;

    private Integer createTime;


}
