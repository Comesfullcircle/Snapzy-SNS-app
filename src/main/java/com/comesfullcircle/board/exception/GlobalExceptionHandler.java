package com.comesfullcircle.board.exception;

import com.comesfullcircle.board.model.error.ClientErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Logger 선언
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<ClientErrorResponse> handleClientErrorException(ClientErrorException e) {
        return new ResponseEntity<>(
                new ClientErrorResponse(e.getStatus(), e.getMessage()), e.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ClientErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        // 명확한 메시지 제공
        String message = "Invalid or missing request body. Please check the request.";
        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ClientErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        // 요청 유효성 검사 실패 시 메시지 생성
        String errorMsg = e.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList()
                .toString();

        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.BAD_REQUEST, errorMsg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ClientErrorResponse> handleRuntimeException(RuntimeException e) {
        // 로깅 추가
        logger.error("Unexpected RuntimeException occurred", e);

        String message = "An unexpected error occurred. Please try again later.";
        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ClientErrorResponse> handleGenericException(Exception e) {
        // 로깅 추가
        logger.error("Unexpected Exception occurred", e);

        String message = "An internal server error occurred. Please contact support.";
        return new ResponseEntity<>(
                new ClientErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
