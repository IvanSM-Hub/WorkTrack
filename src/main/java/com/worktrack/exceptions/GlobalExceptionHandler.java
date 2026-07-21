package com.worktrack.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends Exception {

	@ExceptionHandler(CategoryAlreadyExistsException.class)
	public ResponseEntity<Map<String, Object>> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
		log.error("Category already exists: {}", ex.getMessage());
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleCategoryAlreadyExists(CategoryNotFoundException ex) {
		log.error("Category not found: {}", ex.getMessage());
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(ProjectNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleProjectNotFound(ProjectNotFoundException ex) {
		log.error("Project not found: {}", ex.getMessage());
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message != null && !message.isBlank() ? message : status.getReasonPhrase());
		return ResponseEntity.status(status).body(body);
	}

}
