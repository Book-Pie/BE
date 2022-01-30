package com.bookpie.shop.utils;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class FileUtil {

    public static String save(String filePath, MultipartFile file) throws Exception{
        if(file.isEmpty()){
            throw new FileUploadException("파일이 없습니다.");
        }
        String fileName = new StringBuilder()
                .append(new Date().getTime())
                .append(file.getOriginalFilename())
                .toString();

        try{
            Path path = Paths.get(filePath+fileName);
            Files.write(path,file.getBytes());
            return fileName;
        }catch (Exception e){
            throw new FileUploadException("파일 업로드 중 에러가 발생하였습니다.");
        }
    }

    public static void delete(String filePath,String fileName) throws Exception{
        File file = new File(filePath+fileName);
        if (file.exists()){
            file.delete();
        }
    }
}
