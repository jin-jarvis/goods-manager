package com.jarvis.goods.manager.dto.generic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 조회 필터 Page Criteria
 */
@Data
public class PageCriteria extends BaseCriteria{
    @Schema(description = "페이지 번호")
    private int page = 1;

    @Schema(description = "페이지 사이즈")
    private int size = 10;

    @Schema(hidden = true)
    private int offset= 0;
}
