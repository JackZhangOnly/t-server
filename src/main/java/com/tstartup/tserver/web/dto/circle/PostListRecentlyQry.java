package com.tstartup.tserver.web.dto.circle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostListRecentlyQry {


    @Schema(description = "条数-默认10")
    private int size = 10;
}
