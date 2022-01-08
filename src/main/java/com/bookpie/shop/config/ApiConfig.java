package com.bookpie.shop.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

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
}
