package com.onlinepowers.springmybatis.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import java.util.Map;

public class ApiResponseEntity extends ResponseEntity<Map<String, Object>> {

	public ApiResponseEntity(HttpStatus status) {
		super(status);
	}

	public ApiResponseEntity(Map<String, Object> body, HttpStatus status) {
		super(body, status);
	}

	public ApiResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
		super(headers, status);
	}

	public ApiResponseEntity(Map<String, Object> body, MultiValueMap<String, String> headers, HttpStatus status) {
		super(body, headers, status);
	}

	public ApiResponseEntity(Map<String, Object> body, MultiValueMap<String, String> headers, int rawStatus) {
		super(body, headers, rawStatus);
	}

}
