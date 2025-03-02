package com.jarvis.goods.manager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 구현2번에 대한 상품 통계결과 DTO..
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "상품 통계결과 DTO 2")
@Data
public class GoodsStatsFor2Dto {
    @JsonProperty("최저가")
    @Schema(description = "단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격 결과")
    private  GoodsStatsDto minPriceByBrandAndCategory;

}
