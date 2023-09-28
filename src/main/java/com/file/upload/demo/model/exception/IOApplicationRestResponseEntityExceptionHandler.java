package com.file.upload.demo.model.exception;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.BINDING_ERRORS;
import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;
import static org.springframework.boot.web.error.ErrorAttributeOptions.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class IOApplicationRestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final ErrorAttributes errorAttributes;

    @Autowired
    public IOApplicationRestResponseEntityExceptionHandler(ErrorAttributes errorAttributes) {
        super();
        this.errorAttributes = errorAttributes;
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<Object> handleFileException(FileException e, WebRequest webRequest) {
        return handleExceptionInternal(e, null, new HttpHeaders(), e.getStatus(), webRequest);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e, WebRequest webRequest) {
        return handleExceptionInternal(e, null, new HttpHeaders(), BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, @Nullable Object additionalBody, @NonNull HttpHeaders headers, HttpStatus status, @NonNull WebRequest request) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, of(BINDING_ERRORS, MESSAGE));
        body.put("error", status.getReasonPhrase());
        body.put("path", request.getDescription(false));
        body.put("status", status.value());

        if (additionalBody instanceof Map) {
            Map<?, ?> additionalBodyAsMap = (Map<?, ?>) additionalBody;
            additionalBodyAsMap.forEach((key, value) -> body.put(String.valueOf(key), value));
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, of(BINDING_ERRORS, MESSAGE));
        body.put("error", status.getReasonPhrase());
        body.put("path", request.getDescription(false));
        body.put("status", status.value());
        body.put("message", "INVALID_REQUEST");
        body.put("code", "INVALID_REQUEST");
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

}
