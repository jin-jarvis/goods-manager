package com.jarvis.goods.manager.controller;


import com.jarvis.goods.manager.dto.GoodsCriteria;
import com.jarvis.goods.manager.dto.generic.ResultDto;
import com.jarvis.goods.manager.model.Goods;
import com.jarvis.goods.manager.service.GoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * 상품 메인 컨트롤러
 */
@RequestMapping("/goods")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GoodsController {

    private final GoodsService goodsService;

    /**
     * 1 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회
     * @param brandList
     * @return
     */
    @Operation(summary = "구현 1",
            description = "구현 1 API. 같은 가격의 상품이 추가되면, 카테고리별 상품리스트에는 추가되지만, 총액은 변함이 없음.",
            tags = "1 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회")
    @GetMapping("/getMinPriceBrandByCategory")
    public Mono<ResultDto> getMinPriceBrandByCategory(
            @Parameter(
                    description = "특정 브랜드 제외 brandList. \",\" 로 구분. (원래는 제외가 아니라 포함으로 할려 했으나, 화면없이 API만 사용한다면 이게 편할듯해서..)",
                    schema = @Schema(example = "X,Z"), example = "X,Z")
            @RequestParam(value = "brandList", required = false) List<String> brandList) {
        log.info(">>>>> [ GoodsController : getMinPriceBrandByCategory() ], brandList={}", brandList);
        return goodsService.getMinPriceBrandByCategory(brandList)
                .map(m -> new ResultDto().success(m))
                .switchIfEmpty(Mono.just(new ResultDto().notFound()));
    }

    /**
     * 2 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회
     * @param brandList
     * @return
     */
    @Operation(summary = "구현 2",
            description = "구현 2 API. 같은 가격의 같은 브랜드 상품이 추가되면, 카테고리별 상품리스트에는 추가되지만, 총액은 변함이 없음.",
            tags = "2 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회")
    @GetMapping("/getMinPriceByBrandAndCategory")
    public Mono<ResultDto> getMinPriceByBrandAndCategory(
            @Parameter(
                    description = "특정 브랜드 제외 brandList. \",\" 로 구분. (원래는 제외가 아니라 포함으로 할려 했으나, 화면없이 API만 사용한다면 이게 편할듯해서..)",
                    schema = @Schema(example = "X,Z"), example = "X,Z")
            @RequestParam(value = "brandList", required = false) List<String> brandList) {
        log.info(">>>>> [ GoodsController : getMinPriceByBrandAndCategory() ], brandList={}", brandList);
        return goodsService.getMinPriceByBrandAndCategory(brandList)
                .map(m -> new ResultDto().success(m))
                .switchIfEmpty(Mono.just(new ResultDto().notFound()));
    }

    /**
     * 3 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회
     * @param brandList
     * @return
     */
    @Operation(summary = "구현 3",
            description = "구현 3 API. 같은 가격의 같은 상품이 추가되면, 상품리스트에 추가.",
            tags = "3 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회")
    @GetMapping("/getMinMaxPricesByBrand")
    public Mono<ResultDto> getMinMaxPricesByBrand(
            @Parameter(
                    description = "category 명", schema = @Schema(example = "상의"), example = "상의")
            @RequestParam(value = "category", required = true) String category,
            @Parameter(
                    description = "특정 브랜드 제외 brandList. \",\" 로 구분. (원래는 제외가 아니라 포함으로 할려 했으나, 화면없이 API만 사용한다면 이게 편할듯해서..)",
                    schema = @Schema(example = "X,Z"), example = "X,Z")
            @RequestParam(value = "brandList", required = false) List<String> brandList) {
        log.info(">>>>> [ GoodsController : getMinMaxPricesByBrand() ], category={}, brandList={}", category, brandList);
        return goodsService.getMinMaxPricesByBrand(category, brandList)
                .map(m -> new ResultDto().success(m))
                .switchIfEmpty(Mono.just(new ResultDto().notFound()));
    }

    /**
     * 4-1 ID로 상품 단건 조회
     * @param id
     * @return
     */
    @Operation(summary = "ID로 상품 단건 조회", description = "ID로 상품 단건 조회 API", tags = "4-1 상품 조회")
    @GetMapping("/{id}")
    public Mono<ResultDto> getById(
            @Parameter(description = "상품 ID", required = true, example = "1")
            @PathVariable Long id) {
        log.info(">>>>> [ GoodsController : getById() ], id={}", id);
        return goodsService.findByIdAndIsDelFalse(id)
                .map(m -> new ResultDto().success(m))
                .switchIfEmpty(Mono.just(new ResultDto().notFound()));
    }

    /**
     * 4-1 상품 다건 조회
     * @param criteria
     * @return
     */
    @Operation(summary = "상품 다건 조회", description = "상품 다건 조회 API", tags = "4-1 상품 조회")
    @GetMapping("/list")
    public Mono<ResultDto> getList(
            @Parameter(
                    description = "상품 조회 criteria", required = true,
                    example = "{\"page\":1,\"size\":10,\"category\":\"상의\",\"brand\":\"B\",\"minPrice\":9000, \"maxPrice\":15000}"
            )
            @ModelAttribute GoodsCriteria criteria) {
        log.info(">>>>> [ GoodsController : getList() ], criteria={}", criteria);
        return goodsService.getList(criteria)
                .collectList()
                .map(m -> !m.isEmpty() ? new ResultDto().success(m) : new ResultDto().notFound());
    }

    /**
     * 4-2 상품 단건 추가
     * @param model
     * @return
     */
    @Operation(summary = "상품 단건 추가", description = "상품 단건 추가 API", tags = "4-2 상품 추가")
    @PostMapping("/create")
    public Mono<ResultDto> create(
            @Parameter(
                description = "상품 모델", required = true,
                schema = @Schema(implementation = Goods.class, example = "{\"category\" : \"상의\", \"brand\" : \"Z\", \"price\" : 100}")
            )
            @RequestBody Goods model) {
        log.info(">>>>> [ GoodsController : create() ], model={}", model);
        return goodsService.create(model)
                .map(m -> new ResultDto().success(1))
                .switchIfEmpty(Mono.just(new ResultDto().notFound()));
    }

    /**
     * 4-2 상품 다건 추가
     * @param modelList
     * @return
     */
    @Operation(summary = "상품 다건 추가", description = "상품 다건 추가 API", tags = "4-2 상품 추가")
    @PostMapping("/createList")
    public Mono<ResultDto> createList(
            @Parameter(
                description = "상품 모델 List", required = true,
                schema = @Schema(type = "array", implementation = Goods.class,
                    example = "[{\"category\" : \"상의\", \"brand\" : \"Z\", \"price\" : 100}, {\"category\" : \"상의\", \"brand\" : \"Y\", \"price\" : 200}]")
                )
            @RequestBody List<Goods> modelList) {
        log.info(">>>>> [ GoodsController : createList() ], modelList={}", modelList);
        return goodsService.createList(modelList)
                .collectList()
                .map(m -> !m.isEmpty() ? new ResultDto().success(m.size()) : new ResultDto().notFound());
    }

    /**
     * 4-3 상품 단건 업데이트
     * @param model
     * @return
     */
    @Operation(summary = "상품 단건 업데이트", description = "상품 단건 업데이트 API (ID 기준으로 업데이트)", tags = "4-3 상품 업데이트")
    @PostMapping("/update")
    public Mono<ResultDto> update(
            @Parameter(
                    description = "상품 모델 (id 필수)", required = true,
                    schema = @Schema(implementation = Goods.class, example = "{\"id\" : 73, \"category\" : \"바지\", \"brand\" : \"U\", \"price\" : 200}")
            )
            @RequestBody Goods model) {
        log.info(">>>>> [ GoodsController : update() ], model={}", model);
        return goodsService.update(model)
                .map(m -> new ResultDto().success(1))
                .switchIfEmpty(Mono.just(new ResultDto().notFound()));
    }

    /**
     * 4-3 상품 다건 업데이트
     * @param modelList
     * @return
     */
    @Operation(summary = "상품 다건 업데이트", description = "상품 다건 업데이트 API (ID 기준으로 업데이트)", tags = "4-3 상품 업데이트")
    @PostMapping("/updateList")
    public Mono<ResultDto> updateList(
            @Parameter(
                description = "상품 모델 List (id 필수)", required = true,
                schema = @Schema(type = "array", implementation = Goods.class, example = "[{\"id\":73,\"category\":\"바지\",\"brand\":\"U\",\"price\":200},{\"id\":74,\"category\":\"가방\",\"brand\":\"X\",\"price\":500}]")
            )
            @RequestBody List<Goods> modelList) {
        log.info(">>>>> [ GoodsController : updateList() ], modelList={}", modelList);
        return goodsService.updateList(modelList)
                .collectList()
                .map(m -> !m.isEmpty() ? new ResultDto().success(m.size()) : new ResultDto().notFound());
    }

    /**
     * 4-4 상품 단건 삭제
     * @param model
     * @return
     */
    @Operation(summary = "상품 단건 삭제", description = "상품 단건 삭제 API (ID 기준으로 is_del=true 처리)", tags = "4-4 상품 삭제")
    @PostMapping("/delete")
    public Mono<ResultDto> delete(
            @Parameter(
                description = "상품 모델 (id 필수)", required = true,
                schema = @Schema(implementation = Goods.class, example = "{\"id\" : 1}")
            )
            @RequestBody Goods model) {
        log.info(">>>>> [ GoodsController : delete() ], model={}", model);
        return goodsService.delete(model)
                .map(m -> new ResultDto().success(1))
                .switchIfEmpty(Mono.just(new ResultDto().notFound()));
    }

    /**
     * 4-4 상품 다건 삭제
     * @param modelList
     * @return
     */
    @Operation(summary = "상품 다건 삭제", description = "상품 다건 삭제 API (ID 기준으로 is_del=true 처리)", tags = "4-4 상품 삭제")
    @PostMapping("/deleteList")
    public Mono<ResultDto> deleteList(
            @Parameter(
                description = "상품 모델 List (id 필수)", required = true,
                schema = @Schema(type = "array", implementation = Goods.class, example = "[{\"id\":2},{\"id\":3}]")
            )
            @RequestBody List<Goods> modelList) {
        log.info(">>>>> [ GoodsController : deleteList() ], modelList={}", modelList);
        return goodsService.deleteList(modelList)
                .collectList()
                .map(m -> !m.isEmpty() ? new ResultDto().success(m.size()) : new ResultDto().notFound());
    }

}
