package com.jarvis.goods.manager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 상품 통계결과 DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "상품 통계결과 DTO")
@Data
public class GoodsStatsDto {

    // 1. 구현1
    @JsonProperty("카테고리별_상품리스트")
    @Schema(description = "카테고리 별 최저가격 브랜드와 상품 가격 결과")
    private Map<String, List<GoodsStatsListDto>> minPriceBrandByCategoryMap;

    @JsonProperty("총액")
    @Schema(description = "총액")
    private Integer totalPrice;

    //2. 구현2
    @JsonProperty("브랜드")
    @Schema(description = "최저가 브랜드")
    private String minPriceBrand;

    @JsonProperty("카테고리")
    @Schema(description = "단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격 List")
    private List<GoodsStatsListDto> minPriceByBrandAndCategoryList;

    //2. 구현3
    @Schema(description = "카테고리")
    private String paramCategory;

    @JsonProperty("최저가")
    @Schema(description = "카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 :: 최저가 List")
    private List<GoodsStatsListDto> minPricesByBrandList;

    @JsonProperty("최고가")
    @Schema(description = "카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 :: 최고가 List")
    private List<GoodsStatsListDto> maxPricesByBrandList;

}
