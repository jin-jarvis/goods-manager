package com.jarvis.goods.manager.dto;

import com.jarvis.goods.manager.dto.generic.PageCriteria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 상품 조회 필터 Criteria
 */
@Data
public class GoodsCriteria extends PageCriteria {
    @Schema(description = "카테고리")
    private String category;

    @Schema(description = "브랜드")
    private String brand;

    @Schema(description = "최소 가격")
    private Integer minPrice;

    @Schema(description = "최대 가격")
    private Integer maxPrice;

    @Schema(description = "가격")
    private Integer price;
}
