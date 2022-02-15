package com.bookpie.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {
    @Value("${external.aladin.key}")
    private String aladinAPI;

    @Value("${external.jungbo.key}")
    private String jungboAPI;

    @Value("${external.iamport.code}")
    private String iamportCode;

    @Value("${external.iamport.key}")
    private String iamportKey;

    @Value("${external.iamport.secret}")
    private String iamportSecret;

    @Value("${spring.mail.username}")
    private String adminMail;

    public String getAladinAPI() {
        return this.aladinAPI;
    }
    public String getJungboAPI() {
        return this.jungboAPI;
    }
    public String getIamportCode() {
        return this.iamportCode;
    }
    public String getIamportKey() {
        return this.iamportKey;
    }
    public String getIamportSecret() {
        return this.iamportSecret;
    }
    public String getAdminMail() {return this.adminMail;}
}
