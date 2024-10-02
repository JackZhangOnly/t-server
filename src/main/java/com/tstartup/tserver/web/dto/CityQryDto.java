package com.tstartup.tserver.web.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @ClassName CityQryDto
 * @Description
 * @Author zhang
 * @Date 2024/8/31 21:23
 * @Version 1.0
 */
@Data
public class CityQryDto {

    @Schema(description = "父级id; 0表示无父级，为国家")
    private Integer parentId = 0;


}
