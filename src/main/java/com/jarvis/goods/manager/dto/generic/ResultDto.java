package com.jarvis.goods.manager.dto.generic;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Rest Controller Json 데이터 포맷
 */
@Data
public class ResultDto<T> implements Serializable {
	@Serial
	private static final long serialVersionUID = -5392967769780352480L;

	@Schema(description = "API결과코드", example = "0000")
	private String code = ApiResult.SUCCESS.getCode();

	@Schema(description = "API메세지", example = "Success")
	private String message = ApiResult.SUCCESS.getMessage();

	@Schema(description = "데이터")
	private T data;

	public ResultDto() {
	}

	public ResultDto success(T data) {
		this.data = data;
		return this;
	}

	public ResultDto fail() {
		this.code = ApiResult.FAIL.getCode();
		this.message = ApiResult.FAIL.getMessage();
		return this;
	}

	public ResultDto fail(String code, String message) {
		this.code = code;
		this.message = message;
		return this;
	}

	public ResultDto notFound() {
		this.code = ApiResult.NOT_FOUND.getCode();
		this.message = ApiResult.NOT_FOUND.getMessage();
		return this;
	}

}
