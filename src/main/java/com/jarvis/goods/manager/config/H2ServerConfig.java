package com.jarvis.goods.manager.config;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * H2 console 화면 설정
 */
@Slf4j
@Configuration
public class H2ServerConfig {
    @Value("${h2.console.port}")
    private Integer port;

    private Server webServer;

    @EventListener(ContextRefreshedEvent.class)
    public void start() throws java.sql.SQLException {
        log.info("started h2 console at port {}", port);
        this.webServer = Server.createWebServer("-webPort", port.toString());
        this.webServer.start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        log.info("stopped h2 console at port {}", port);
        this.webServer.stop();
    }
}
