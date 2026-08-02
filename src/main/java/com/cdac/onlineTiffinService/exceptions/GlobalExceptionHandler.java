package com.cdac.onlineTiffinService.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException e , HttpServletRequest req)
	{
		log.warn("Resource not found at [{}]: {}", req.getRequestURI(), e.getMessage());
		ApiError error = new ApiError(
				LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.name(),
				e.getMessage(),
				req.getRequestURI()
				);
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND); //404
		
	}
	@ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateResource(
            DuplicateResourceException ex,
            HttpServletRequest request) {

        log.warn("Duplicate resource at [{}]: {}", request.getRequestURI(), ex.getMessage());
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.name(),
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT); // 409
        
       
    }
	
	@ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbidden(
            ForbiddenException ex,
            HttpServletRequest request) {

        log.warn("Forbidden access attempt at [{}]: {}", request.getRequestURI(), ex.getMessage());
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name(),
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN); //403
    }

	@ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request) {

        log.warn("Bad request at [{}]: {}", request.getRequestURI(), ex.getMessage());
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); //400
    }
	
	@ExceptionHandler(MyIOException.class)
	public ResponseEntity<ApiError> handleMyIOException(
	        MyIOException ex,
	        HttpServletRequest request) {

	    log.error("IO error while processing [{}]", request.getRequestURI(), ex);
	    ApiError error = new ApiError(
	            LocalDateTime.now(),
	            HttpStatus.INTERNAL_SERVER_ERROR.value(),
	            HttpStatus.INTERNAL_SERVER_ERROR.name(),
	            ex.getMessage(),
	            request.getRequestURI());

	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
	
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unhandled exception while processing [{}]", request.getRequestURI(), ex);
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR); //500
        
        
    }
	
	// DTO Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        log.warn("Validation failed: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // PathVariable / RequestParam Validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        log.warn("Constraint violation at [{}]: {}", request.getRequestURI(), ex.getMessage());
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
