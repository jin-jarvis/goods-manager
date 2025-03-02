package com.jarvis.goods.manager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 상품 통계 관련 리스트 DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GoodsStatsListDto {

    @JsonProperty("상품아이디")
    @Schema(description = "상품아이디")
    private Long id;

    @JsonProperty("카테고리")
    @Schema(description = "카테고리")
    private String category;

    @JsonProperty("브랜드")
    @Schema(description = "브랜드")
    private String brand;

    @JsonProperty("가격")
    @Schema(description = "가격")
    private Integer price;

    @JsonIgnore
    @Schema(description = "최소/최대 가격타입")
    private String priceType;

}
