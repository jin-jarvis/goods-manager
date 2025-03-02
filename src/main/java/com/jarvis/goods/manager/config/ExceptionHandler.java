package com.jarvis.goods.manager.config;

import com.jarvis.goods.manager.dto.generic.ApiResult;
import com.jarvis.goods.manager.dto.generic.ResultDto;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 ExceptionHandler
 */
@Slf4j
@NoArgsConstructor
@RestControllerAdvice(basePackages = "com.jarvis.goods.manager.controller")
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.OK)
    public ResultDto handleException(Exception e) {
        log.error("Exception, during API, {}", e.getMessage());
        return new ResultDto().fail(ApiResult.FAIL.getCode(), e.getMessage());
    }

}
