package com.bookpie.shop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Image {
    @Id @GeneratedValue
    @Column(name = "image_id")
    private  Long id;
    private String fileName;


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
