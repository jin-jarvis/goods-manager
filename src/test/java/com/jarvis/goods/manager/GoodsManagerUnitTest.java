package com.jarvis.goods.manager;

import com.jarvis.goods.manager.controller.GoodsController;
import com.jarvis.goods.manager.dto.generic.ResultDto;
import com.jarvis.goods.manager.model.Goods;
import com.jarvis.goods.manager.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * 단위 테스트
 */
@Slf4j
@WebFluxTest(GoodsController.class)
public class GoodsManagerUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GoodsService goodsService;

    @BeforeEach
    void setUp() {
    }

    /**
     * 조회 테스트
     */
    @Test
    void testGoodsId() {
        long id = 1L;

        // mockGoods
        Goods mockGoods = new Goods();
        mockGoods.setId(id);
        mockGoods.setCategory("상의");
        mockGoods.setBrand("Z");
        mockGoods.setPrice(100);

        when(goodsService.findByIdAndIsDelFalse(id)).thenReturn(Mono.just(mockGoods));

        webTestClient.get().uri("/goods/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResultDto<Goods>>() {})
                .value(v -> {
                    log.info("=== Expected ===");
                    log.info("{}", mockGoods);
                    log.info("=== Actual ===");
                    log.info("{}", v.getData());

                    assertEquals(mockGoods, v.getData());
                });
    }

    /**
     * 생성 테스트
     */
    @Test
    void testGoodsCreate() {
        long id = 1L;

        // mockGoods
        Goods mockGoods = new Goods();
        mockGoods.setId(id);
        mockGoods.setCategory("상의");
        mockGoods.setBrand("Z");
        mockGoods.setPrice(100);

        when(goodsService.create(mockGoods)).thenReturn(Mono.just(mockGoods));

        webTestClient.post().uri("/goods/create")
                .body(Mono.just(mockGoods), Goods.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResultDto<Long>>() {})
                .value(v -> {
                    log.info("=== Expected ===");
                    log.info("{}", 1);
                    log.info("=== Actual ===");
                    log.info("{}", v.getData());

                    assertEquals(1, v.getData());
                });
    }
}
