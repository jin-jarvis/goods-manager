package com.jarvis.goods.manager.repository;

import com.jarvis.goods.manager.dto.GoodsCriteria;
import com.jarvis.goods.manager.dto.GoodsStatsListDto;
import com.jarvis.goods.manager.model.Goods;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * GoodsRepository
 */
public interface GoodsRepository extends R2dbcRepository<Goods, Long> {

    Mono<Goods> findByIdAndIsDelFalse(Long id);

    Flux<Goods> findByIdInAndIsDelFalse(List<Long> ids);

    @Query("SELECT * FROM goods " +
            "WHERE 1=1 " +
            "AND is_del = false " +
            "AND (:#{#criteria.id} is null or id = :#{#criteria.id}) " +
            "AND (:#{#criteria.category} is null or category = :#{#criteria.category}) " +
            "AND (:#{#criteria.brand} is null or brand = :#{#criteria.brand}) " +
            "AND (:#{#criteria.price} is null or price = :#{#criteria.price}) " +
            "AND (:#{#criteria.minPrice} is null or price >= :#{#criteria.minPrice}) " +
            "AND (:#{#criteria.maxPrice} is null or price <= :#{#criteria.maxPrice}) " +
            "ORDER BY price " +
            "LIMIT :#{#criteria.size} OFFSET :#{#criteria.offset} "
    )
    Flux<Goods> getList(@Param(value = "criteria") GoodsCriteria criteria);

    @Query("WITH MinPricesByCategory AS ( " +
            "   SELECT " +
            "       category, " +
            "       MIN(price) AS min_price " +
            "   FROM goods " +
            "   GROUP BY category " +
            ") " +
            "SELECT " +
            "   g.id," +
            "   g.category, " +
            "   g.brand, " +
            "   g.price " +
            "FROM " +
            "   MinPricesByCategory mpbc " +
            "   JOIN goods g ON mpbc.min_price = g.price AND mpbc.category = g.category " +
            "WHERE " +
            "   1=1 " +
            "   AND g.brand NOT IN (:brandList) "
    )
    Flux<GoodsStatsListDto> getMinPriceBrandByCategory(@Param(value = "brandList") List<String> brandList);

    @Query("WITH MinPricesByCategoryBrand AS ( " +
            "   SELECT " +
            "       brand, " +
            "       category, " +
            "       MIN(price) AS min_price " +
            "   FROM goods " +
            "   GROUP BY brand, category " +
            "), " +
            "SumPricesByBrand AS ( " +
            "   SELECT " +
            "       brand, " +
            "       SUM(min_price) AS total_price " +
            "   FROM MinPricesByCategoryBrand " +
            "   GROUP BY brand " +
            "), " +
            "MinPricesBrand AS( " +
            "   SELECT " +
            "       brand, " +
            "       total_price " +
            "FROM " +
            "   SumPricesByBrand " +
            "   WHERE " +
            "   total_price = (SELECT MIN(total_price) FROM SumPricesByBrand) " +
            ") " +
            "SELECT " +
            "   g.id, " +
            "   g.brand, " +
            "   g.category, " +
            "   g.price, " +
            "   mpb.total_price " +
            "FROM " +
            "   MinPricesBrand mpb  " +
            "   JOIN MinPricesByCategoryBrand mpbcb ON mpb.brand = mpbcb.brand " +
            "   JOIN goods g ON mpb.brand = g.brand AND mpbcb.min_price = g.price " +
            "WHERE " +
            "   1=1 " +
            "   AND g.brand NOT IN (:brandList) "
    )
    Flux<GoodsStatsListDto> getMinPriceByBrandAndCategory(@Param(value = "brandList") List<String> brandList);

    @Query("WITH MinMaxPricesByCategory AS ( " +
            "   SELECT " +
            "       category, " +
            "       MIN(price) AS min_price, " +
            "       MAX(price) AS max_price " +
            "   FROM goods " +
            "   GROUP BY category " +
            "), " +
            "MinMaxPricesUnion AS ( " +
            "   SELECT  " +
            "       category,  " +
            "       'min_price' AS price_type,  " +
            "       min_price AS price " +
            "   FROM MinMaxPricesByCategory mmpbc " +
            "   UNION ALL " +
            "   SELECT  " +
            "       category,  " +
            "       'max_price' AS price_type,  " +
            "       max_price AS price " +
            "   FROM MinMaxPricesByCategory " +
            ")  " +
            "SELECT  " +
            "   g.id, " +
            "   g.brand, " +
            "   g.category, " +
            "   g.price, " +
            "   mmpu.price_type " +
            "FROM  " +
            "   MinMaxPricesUnion mmpu  " +
            "   JOIN goods g ON mmpu.category = g.category AND mmpu.price = g.price " +
            "WHERE " +
            "   1=1 " +
            "   AND g.brand NOT IN (:brandList) " +
            "   AND (:category is null or g.category = :category) "
    )
    Flux<GoodsStatsListDto> getMinMaxPricesByBrand(@Param(value = "category") String category,
                                                   @Param(value = "brandList") List<String> brandList);

}
