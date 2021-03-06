package com.bookpie.shop.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Point implements Serializable {

    private int totalPoint;
    private int usedPoint;
    private int holdPoint;


    public void chargePoint(int point){
        totalPoint+=point;
        holdPoint+=point;
    }

    public void rollback(int point){
        usedPoint-=point;
        holdPoint+=point;
    }
    public void usePoint(int point) {
        if (holdPoint >= point) {
            usedPoint += point;
            holdPoint -= point;
        }
    }

    public void addSalePoint(int point){
        holdPoint+=point;
    }
    public static Point createDefaultPoint(){
        return new Point(0,0,0);
    }
}
