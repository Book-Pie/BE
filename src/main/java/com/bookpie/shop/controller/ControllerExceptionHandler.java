package com.bookpie.shop.controller;

import com.bookpie.shop.utils.ApiUtil.ApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletException;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.bookpie.shop.utils.ApiUtil.error;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class,EntityNotFoundException.class,FileNotFoundException.class})
    protected ResponseEntity handleUsernameNotFound(Exception e){
        log.debug(e.getMessage());
        return new ResponseEntity(error(e, HttpStatus.NOT_FOUND),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IllegalArgumentException.class, JsonProcessingException.class})
    protected ResponseEntity handleIllegalArgumenException(Exception e){
        log.debug(e.getMessage());
        return new ResponseEntity(error(e,HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(FileUploadException.class)
    protected ResponseEntity handleFileException(Exception e){
        log.debug(e.getMessage());
        return new ResponseEntity(error(e,HttpStatus.INTERNAL_SERVER_ERROR),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity hadleValidException(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        log.debug(message);
        return new ResponseEntity(error(message,HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({IOException.class, ServletException.class})
    protected ResponseEntity handleFilterException(Exception e){
        log.debug(e.getMessage());
        return new ResponseEntity(error(e,HttpStatus.INTERNAL_SERVER_ERROR),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
