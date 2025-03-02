package com.jarvis.goods.manager.service;

import com.jarvis.goods.manager.dto.GoodsCriteria;
import com.jarvis.goods.manager.dto.GoodsStatsDto;
import com.jarvis.goods.manager.dto.GoodsStatsFor2Dto;
import com.jarvis.goods.manager.dto.GoodsStatsListDto;
import com.jarvis.goods.manager.model.Goods;
import com.jarvis.goods.manager.repository.GoodsRepository;
import com.jarvis.goods.manager.service.generic.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 상품 서비스
 */
@Service
public class GoodsService extends BaseService<Goods> {

    @Autowired
    private GoodsRepository goodsRepository;

    public GoodsService(GoodsRepository goodsRepository) {
        super(goodsRepository);
        this.goodsRepository = goodsRepository;
    }

    public Mono<Goods> findByIdAndIsDelFalse(Long id) {
        return goodsRepository.findByIdAndIsDelFalse(id);
    }

    public Flux<Goods> findByIdInAndIsDelFalse(List<Long> ids) {
        return goodsRepository.findByIdInAndIsDelFalse(ids);
    }

    /**
     * 구현 1 : 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
     * 1. 카테고리별 최저가격 상품을 조회한다
     * 2. 해당 상품들의 메타정보와 조인한다.
     * 3. 해당 상품들의 총합을 구한다.
     * 4. 계산된 정보들은 DTO에 담아 리턴한다.
     * @param brandList
     * @return
     */
    public Mono<GoodsStatsDto> getMinPriceBrandByCategory(List<String> brandList) {
        if(brandList == null) brandList = new ArrayList<>();

        return goodsRepository.getMinPriceBrandByCategory(brandList)
                .collectList()
                .flatMap(goodsList -> {
                    GoodsStatsDto goodsStatsDto = null;
                    if (!goodsList.isEmpty()) {
                        goodsStatsDto = new GoodsStatsDto();
                        // <Category 명, 최저가격 상품리스트> Map 만들기
                        Map<String, List<GoodsStatsListDto>> categoryMap = goodsList.stream().collect(
                                Collectors.groupingBy(g -> g.getCategory()));
                        goodsStatsDto.setMinPriceBrandByCategoryMap(categoryMap);
                        // 위에서 만든 Map의 카테고리별 하나의 가격만 뽑아서 총합구하기
                        int totalPrice = categoryMap.entrySet().stream()
                                .map(m2 -> m2.getValue())
                                .mapToInt(m3 -> m3.getFirst().getPrice())
                                .sum();
                        goodsStatsDto.setTotalPrice(totalPrice);
                    }
                    return Mono.justOrEmpty(goodsStatsDto);
                });
    }

    /**
     * 구현 2 :단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회
     * 1. 카테고리별 촤저가격 상품을 조회한다.
     * 2. 해당 상품들의 브랜드별 가격의 총합을 구한다.
     * 3. 해당 상품들의 총합이 가장 낮은 브랜드를 구한다.
     * 4. 해당 브랜드의 해당 상품들의 메타정보와 조인한다.
     * 5. 해당 상품들의 총합을 구한다.
     * 6. 계산된 정보들은 DTO에 담아 리턴한다.
     * @param brandList
     * @return
     */
    public Mono<GoodsStatsFor2Dto> getMinPriceByBrandAndCategory(List<String> brandList) {
        if(brandList == null) brandList = new ArrayList<>();

        return goodsRepository.getMinPriceByBrandAndCategory(brandList)
                .collectList()
                .flatMap(goodsList -> {
                    GoodsStatsFor2Dto goodsStatsFor2Dto = null;

                    if (!goodsList.isEmpty()) {
                        goodsStatsFor2Dto = new GoodsStatsFor2Dto();
                        GoodsStatsDto goodsStatsDto = new GoodsStatsDto();
                        // <"카테고리", 카테고리 총합 최저가격 브랜드 상품리스트> Map 만들기
                        goodsStatsDto.setMinPriceByBrandAndCategoryList(goodsList);
                        // 브랜드
                        goodsStatsDto.setMinPriceBrand(goodsList.getFirst().getBrand());
                        // 카테고리별 하나의 가격만 뽑아서 총합구하기
                        int totalPrice = goodsList.stream().collect(
                                Collectors.groupingBy(g -> g.getCategory()))
                                .entrySet().stream()
                                .map(m2 -> m2.getValue())
                                .mapToInt(m3 -> m3.getFirst().getPrice())
                                .sum();
                        goodsStatsDto.setTotalPrice(totalPrice);
                        goodsStatsFor2Dto.setMinPriceByBrandAndCategory(goodsStatsDto);
                    }

                    return Mono.justOrEmpty(goodsStatsFor2Dto);
                });
    }

    /**
     * 구현 3 :카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
     * 1. 카테고리별 촤저가격, 최고가격 상품을 조회한다.
     * 2. 열데이터를 행데이터로 바꾼다.
     * 3. 해당 상품들의 메타정보와 조인한다.
     * 4. 계산된 정보들은 DTO에 담아 리턴한다.
     * @param category
     * @param brandList
     * @return
     */
    public Mono<GoodsStatsDto> getMinMaxPricesByBrand(String category, List<String> brandList) {
        if (brandList == null) brandList = new ArrayList<>();

        return goodsRepository.getMinMaxPricesByBrand(category, brandList)
                .collectList()
                .flatMap(goodsList -> {
                    GoodsStatsDto goodsStatsDto = null;

                    if (!goodsList.isEmpty()) {
                        goodsStatsDto = new GoodsStatsDto();
                        goodsStatsDto.setParamCategory(category);
                        goodsStatsDto.setMinPricesByBrandList(goodsList.stream()
                                .filter(f -> "min_price".equals(f.getPriceType())).toList());
                        goodsStatsDto.setMaxPricesByBrandList(goodsList.stream()
                                .filter(f -> "max_price".equals(f.getPriceType())).toList());
                    }

                    return Mono.justOrEmpty(goodsStatsDto);
                });
    }

    public Flux<Goods> getList(GoodsCriteria criteria) {
        // paging
        criteria.setOffset((criteria.getPage() - 1) * criteria.getSize());
        return goodsRepository.getList(criteria);
    }

    @Transactional
    public Mono<Goods> update(Goods model) {
        return goodsRepository.findByIdAndIsDelFalse(model.getId()).flatMap(m -> {
            if (StringUtils.isNotEmpty(model.getCategory())) {m.setCategory(model.getCategory());}
            if (StringUtils.isNotEmpty(model.getBrand())) {m.setBrand(model.getBrand());}
            if (model.getPrice() != null) {m.setPrice(model.getPrice());}

            m.setModifiedAt(LocalDateTime.now());
            return goodsRepository.save(m);
        });
    }

    @Transactional
    public Flux<Goods> updateList(List<Goods> modelList) {
        // List to Map
        Map<Long, Goods> modelMap = new HashMap<>();
        modelList.forEach(f -> modelMap.put(f.getId(), f));

        return goodsRepository.findByIdInAndIsDelFalse(modelList.stream().map(m -> m.getId()).toList())
                .map(m -> {
                    Goods model = modelMap.get(m.getId());
                    if (StringUtils.isNotEmpty(model.getCategory())) {m.setCategory(model.getCategory());}
                    if (StringUtils.isNotEmpty(model.getBrand())) {m.setBrand(model.getBrand());}
                    if (model.getPrice() != null) {m.setPrice(model.getPrice());}

                    m.setModifiedAt(LocalDateTime.now());
                    return m;
                })
                .collectList()
                .flatMapMany(ml -> goodsRepository.saveAll(ml));
    }

    @Transactional
    public Mono<Goods> delete(Goods model) {
        return goodsRepository.findByIdAndIsDelFalse(model.getId()).flatMap(m -> {
            m.setIsDel(true);
            m.setModifiedAt(LocalDateTime.now());
            return goodsRepository.save(m);
        });
    }

    @Transactional
    public Flux<Goods> deleteList(List<Goods> modelList) {
        return goodsRepository.findByIdInAndIsDelFalse(modelList.stream().map(m -> m.getId()).toList())
                .map(m -> {
                    m.setIsDel(true);
                    m.setModifiedAt(LocalDateTime.now());
                    return m;
                })
                .collectList()
                .flatMapMany(ml -> goodsRepository.saveAll(ml));
    }

}
