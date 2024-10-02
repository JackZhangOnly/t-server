package com.tstartup.tserver.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName CommonTypeQryDto
 * @Description
 * @Author zhang
 * @Date 2024/8/17 16:51
 * @Version 1.0
 */
@Data
public class CommonTypeQryDto implements Serializable {

    @Schema(description = "业务类型；tripType、tag、keyword、articleType、transportType")
    @NotNull(message = "typeIdentity is required")
    private String typeIdentity;
}
