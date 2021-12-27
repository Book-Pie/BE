package com.bookpie.shop.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Category {
    // 대분류
    NOVELS("소설"),
    ESSAY("수필"),
    CARTOON("만화"),
    SELF_DEVELOPMENT("자기계발"),
    HISTORY("역사"),
    SCIENCE("과학"),
    ETC("기타"),


    // 소설 소분류
    DETECTIVE("추리"), HORROR("호러"), MARTIAL_ARTS("무협"), ACTION("액션"),
    ROMANCE("로맨스"), SCIENCE_NOVEL("과학"),

    //수필 소분류
    FOOD("음식"), TRAVEL("여행"), READING("독서"), RELIGION("종교"),ART("예술"),

    //만화 소분류
    PURE_LOVE("순정"),SPORTS("스포츠"),COOKING("요리"),COMIC("코믹"),

    //자기계발 소분류
    SUCCESS("성공"),RELATIONSHIP("인간관계"),LEADERSHIP("리더십"),

    //역사 소분류
    KOREAN_HISTORY("한국사"),MODERN_HISTORY("근현대사"),CHINESE_HISTORY("중국사"),JAPANESE_HISTORY("일본사"),
    ASIAN_HISTORY("아시아사"),WESTERN_HISTORY("서양사"),

    //과학 소분류
    BASIC_SCIENCE("기초과학"),BIOLOGY("생명과학"),MEDICINE("의학"),PHYSICS("물리학"),
    ASTRONOMY("천문학"),CHEMISTRY("화학"),ENGINEERING("공학");



    private String kr;

    Category(String kr){
        this.kr = kr;
    }

    public String getKr() {
        return kr;
    }

    @JsonCreator
    public static Category nameOf(String name){
        for (Category category: Category.values()){
            if (category.getKr().equals(name)){
                return category;
            }
        }
        return null;
    }
}
