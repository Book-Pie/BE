package com.bookpie.shop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Tag {

    @Id @GeneratedValue
    @Column(name = "tag_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "tag")
    private Set<BookTag> bookTags = new HashSet<>();

    public Tag(String name){
        this.name = name;
    }
}
