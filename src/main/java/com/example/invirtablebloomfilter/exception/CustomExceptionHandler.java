package com.example.invirtablebloomfilter.exception;

import com.example.invirtablebloomfilter.entity.response.CommonResponse;
import com.example.invirtablebloomfilter.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> exceptionHandling(
            Exception ex, WebRequest request) {
        log.error("internal error:", ex);
        CommonResponse response = CommonResponse.builder()
                .code(Constant.INTERNAL_ERROR)
                .message(Constant.MESSAGE_MAP.get(Constant.INTERNAL_ERROR))
                .build();

        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.OK, request);
    }
}
