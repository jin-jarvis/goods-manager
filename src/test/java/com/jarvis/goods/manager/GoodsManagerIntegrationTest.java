package com.jarvis.goods.manager;

import com.jarvis.goods.manager.dto.generic.ResultDto;
import com.jarvis.goods.manager.model.Goods;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 통합 테스트
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GoodsManagerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
    }

    /**
     * 생성 + 조회 테스트
     */
    @Test
    void testGoodsId() {
        Goods testGoods = new Goods();
        testGoods.setCategory("테스트_카테고리_1234_5678");
        testGoods.setBrand("테스트_브랜드_1234_5678");
        testGoods.setPrice(-10000);

        webTestClient.post().uri("/goods/create")
                .body(Mono.just(testGoods), Goods.class)
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

        webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/goods/list")
                        .queryParam("category", testGoods.getCategory())
                        .queryParam("brand", testGoods.getBrand())
                        .queryParam("price", testGoods.getPrice())
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ResultDto<List<Goods>>>() {})
                .value(v -> {
                    log.info("=== Expected ===");
                    log.info("{}", testGoods);
                    log.info("=== Actual ===");
                    log.info("{}", v.getData().getFirst());

                    assertEquals(testGoods, v.getData().getFirst());
                });
    }

}
