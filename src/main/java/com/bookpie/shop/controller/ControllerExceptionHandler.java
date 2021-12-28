package com.bookpie.shop.controller;

import com.bookpie.shop.utils.ApiUtil.ApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

import java.io.FileNotFoundException;

import static com.bookpie.shop.utils.ApiUtil.error;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class,EntityNotFoundException.class,FileNotFoundException.class})
    protected ApiResult handleUsernameNotFound(Exception e){
        return error(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IllegalArgumentException.class, JsonProcessingException.class})
    protected ApiResult handleIllegalArgumenException(Exception e){
        return error(e,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    protected ApiResult handleFileException(Exception e){
        return error(e,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiResult hadleValidException(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return error(message,HttpStatus.BAD_REQUEST);
    }
}
