package com.webflux.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new HashMap<>();
        Throwable throwable = super.getError(request);
        if(throwable instanceof CustomException customException){
            errorAttributes.put("status", customException.getStatus());
            errorAttributes.put("message", customException.getMessage());
        }else {
            errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            errorAttributes.put("message", "Ha ocurrido un error inesperado.");
            errorAttributes.put("exception", throwable.getClass().getSimpleName());
            errorAttributes.put("trace", throwable.getMessage()); // opcional: solo en dev
        }
        return errorAttributes;
    }
}
