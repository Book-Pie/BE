package com.bookpie.shop.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.EAN;
import org.springframework.data.repository.cdi.Eager;

import java.util.ArrayList;
import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Category {


    /* 소설 */
    NOVELS("소설",null),

        DETECTIVE("추리",NOVELS), HORROR("호러",NOVELS), MARTIAL_ARTS("무협",NOVELS),
        ACTION("액션",NOVELS), ROMANCE("로맨스",NOVELS), SCIENCE_NOVEL("과학",NOVELS),

    /* 수필 */
    ESSAY("수필",null),

        FOOD("음식",ESSAY), TRAVEL("여행",ESSAY), READING("독서", ESSAY),
        RELIGION("종교",ESSAY),ART("예술", ESSAY),

    /* 만화 */
    CARTOON("만화",null),

        PURE_LOVE("순정",CARTOON),SPORTS("스포츠",CARTOON),
        COOKING("요리",CARTOON),COMIC("코믹",CARTOON),

    /* 자기계발 */
    SELF_DEVELOPMENT("자기계발",null),

        SUCCESS("성공",SELF_DEVELOPMENT),RELATIONSHIP("인간관계",SELF_DEVELOPMENT),
        LEADERSHIP("리더십",SELF_DEVELOPMENT),

    /* 역사 */
    HISTORY("역사",null),

        KOREAN_HISTORY("한국사",HISTORY),MODERN_HISTORY("근현대사",HISTORY),
        CHINESE_HISTORY("중국사",HISTORY),JAPANESE_HISTORY("일본사",HISTORY),
        ASIAN_HISTORY("아시아사",HISTORY),WESTERN_HISTORY("서양사",HISTORY),

    /* 과학 */
    SCIENCE("과학",null),

        BASIC_SCIENCE("기초과학",SCIENCE),BIOLOGY("생명과학",SCIENCE),MEDICINE("의학",SCIENCE),
        PHYSICS("물리학",SCIENCE), ASTRONOMY("천문학",SCIENCE),
        CHEMISTRY("화학",SCIENCE),ENGINEERING("공학",SCIENCE),

    ETC("기타",null);








    private String kr;
    private Category parent;
    private List<Category> children = new ArrayList<>();

    Category(String kr,Category parent){
        this.kr = kr;
        this.parent = parent;
        if(this.parent!=null){
            this.parent.addChild(this);
        }
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

    public void addChild(Category child){
        for(Category c=this; c!=null; c=c.parent){
            c.children.add(child);
        }
    }

    public List<Category> parents(){
        List<Category> parents = new ArrayList<>();
        for (Category c = this; c.parent!=null; c=c.parent){
            parents.add(c.parent);
        }
        return parents;
    }

    public List<Category> children(){
        return this.children;
    }

    public static List<Category> AllParent() {
        List<Category> parents = new ArrayList<>();
        for(Category c : Category.values()){
            if (c.parent == null) parents.add(c);
        }
        return parents;
    }




}
