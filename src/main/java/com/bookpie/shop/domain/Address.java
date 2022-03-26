package com.bookpie.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
public class Address implements Serializable {

    private String postalCode;
    private String mainAddress;
    private String detailAddress;

    protected Address(){}

    public Address createAddress(String postalCode,String mainAddress, String detailAddress){
        return new Address(postalCode,mainAddress,detailAddress);
    }

}
