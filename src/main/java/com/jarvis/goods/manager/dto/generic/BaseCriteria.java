package com.jarvis.goods.manager.dto.generic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 조회 필터 Base Criteria
 */
@Data
public class BaseCriteria {
    @Schema(description = "상품아이디")
    private Long id;
}
