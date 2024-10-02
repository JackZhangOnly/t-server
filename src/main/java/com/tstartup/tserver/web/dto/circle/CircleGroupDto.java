package com.tstartup.tserver.web.dto.circle;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Administrator
 * @since 2024-10-02
 */
@Data
public class CircleGroupDto implements Serializable {

    @Schema(description = "id")
    private Integer id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "是否热门")
    private Integer isHot;

    @Schema(description = "description")
    private String description;

    private Integer createTime;


}
