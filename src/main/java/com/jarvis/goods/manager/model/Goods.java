package com.jarvis.goods.manager.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;

/**
 * GoodsEntity
 */
@Data
@Table("goods")
public class Goods extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4773715883069366664L;

    @Schema(description = "카테고리")
    private String category;

    @Schema(description = "브랜드")
    private String brand;

    @Schema(description = "가격")
    private Integer price;

}
