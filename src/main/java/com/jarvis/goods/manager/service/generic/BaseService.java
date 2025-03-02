package com.jarvis.goods.manager.service.generic;

import com.jarvis.goods.manager.model.BaseEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * BaseService
 */
public class BaseService<T extends BaseEntity> {

    private R2dbcRepository<T, Long> repository;
    public BaseService(R2dbcRepository<T, Long> repository) {
        this.repository = repository;
    }

    public Mono<T> findById(Long id) {
        return repository.findById(id);
    }

    public Flux<T> findAll() {
        return repository.findAll();
    }

    public Mono<T> create(T entity) {
        entity.setId(null);  // id 입력방지
        return repository.save(entity);
    }

    public Flux<T> createList(List<T> entity) {
        entity.forEach(f -> f.setId(null));  // id 입력방지
        return repository.saveAll(entity);
    }

}
