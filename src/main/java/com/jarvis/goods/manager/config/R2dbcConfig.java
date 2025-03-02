package com.jarvis.goods.manager.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

/**
 * R2dbc 관련 설정
 */
@Slf4j
@Configuration
public class R2dbcConfig {

    /**
     * 스키마 및 초기데이터 initializer
     * r2dbc Spring sql init 옵션은 파일 DB에는 적용이 안되어서 만든 Bean
     */
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        log.info(">>>>> [ R2dbcConfig : ConnectionFactoryInitializer() ], Target Database vendor = {}", connectionFactory.getMetadata().getName());
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        // schema.sql 파일을 추가
        populator.addScript(new ClassPathResource("init/schema.sql"));
        // data.sql 파일을 추가
        populator.addScript(new ClassPathResource("init/data.sql"));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }
}
