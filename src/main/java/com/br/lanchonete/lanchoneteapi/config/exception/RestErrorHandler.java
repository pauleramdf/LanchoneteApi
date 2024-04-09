package com.br.lanchonete.lanchoneteapi.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class RestErrorHandler {

    @Value("${server.error.include-message}")
    private String includeMessage;
    @Value("${server.error.include-exception}")
    private String includeException;
    @Value("${server.error.include-stacktrace}")
    private String includeStacktrace;
    @Value("${server.error.include-binding-errors}")
    private String includeBindingErrors;

    private static final String ALWAYS = "always";

    @ExceptionHandler(DefaultException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> processValidationError(WebRequest webRequest, DefaultException ex) {
        log.error("{}", ex.getMessage());

        DefaultErrorAttributes err = new DefaultErrorAttributes();
        final int resquetScope = 0;
        webRequest.setAttribute("javax.servlet.error.status_code", HttpStatus.BAD_REQUEST.value(), resquetScope);
        return err.getErrorAttributes(webRequest, getErrorAttributeOptions());
    }


    private ErrorAttributeOptions getErrorAttributeOptions() {
        ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.defaults();
        if (ALWAYS.equalsIgnoreCase(includeMessage))
            errorAttributeOptions = errorAttributeOptions.including(ErrorAttributeOptions.Include.MESSAGE);
        if ("true".equalsIgnoreCase(includeException))
            errorAttributeOptions = errorAttributeOptions.including(ErrorAttributeOptions.Include.EXCEPTION);
        if (ALWAYS.equalsIgnoreCase(includeStacktrace))
            errorAttributeOptions = errorAttributeOptions.including(ErrorAttributeOptions.Include.STACK_TRACE);
        if (ALWAYS.equalsIgnoreCase(includeBindingErrors))
            errorAttributeOptions = errorAttributeOptions.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        return errorAttributeOptions;
    }

}
