package com.tstartup.tserver.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author Administrator
 * @since 2024-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TScene implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String enName;

    private String cnName;

    private String description;

    private String sceneType;

    private String imgUrl;

    private String videoUrl;

    private Integer cityId;

    private String address;

    private String seasonToVisit;

    private String monthToVisit;

    private Integer createTime;

    private Integer updateTime;


    private Integer isHot;

}
