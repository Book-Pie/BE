package com.bookpie.shop.controller;

import com.bookpie.shop.utils.ApiUtil;
import com.bookpie.shop.utils.ApiUtil.ApiResult;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static com.bookpie.shop.utils.ApiUtil.*;

@RestController
public class ImageController {

    @Value("${path.image.dev}")
    private String filePath;

    @GetMapping("api/image/{name}")


    public ResponseEntity<Resource> getImage(@PathVariable("name") String name) throws Exception{

        Resource resource = new FileSystemResource(filePath + name);
        if(!resource.exists()){
            throw new FileNotFoundException("파일이 없습니다.");
        }

        Path path = null;
        HttpHeaders header = new HttpHeaders();
        header.setCacheControl(CacheControl.maxAge(120, TimeUnit.SECONDS));
        try{
            path = Paths.get(filePath+name);
            header.add("Content-Type", Files.probeContentType(path));
        }catch (Exception e){
            throw new FileNotFoundException("파일이 없습니다.");
        }
        return new ResponseEntity<Resource>(resource,header, HttpStatus.OK);
    }

}
