package com.jarvis.goods.manager.dto.generic;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * API결과 코드 및 메세지
 */
@Getter
@AllArgsConstructor
public enum ApiResult {
	SUCCESS("0000", "Success"),

	NOT_FOUND("1404", "Resource Not Found"),

	FAIL("9999", "Fail"),

	;

	private final String code;
	private final String message;

}
