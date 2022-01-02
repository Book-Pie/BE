package com.bookpie.shop.repository;

import com.bookpie.shop.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class TagRepository {

    private final EntityManager em;

    public Set<Tag> saveAll(List<Tag> tags){
        Set<Tag> result = new HashSet<>();
        for(Tag tag: tags) {
            result.add(this.save(tag));
        }
        return result;
    }

    public Tag save(Tag tag){
        Optional<Tag> present = findByName(tag.getName());
        if(present.isPresent()){
            return present.get();
        }else{
            em.persist(tag);
            return tag;
        }

    }

    public Optional<Tag> findById(Long id){
        return Optional.ofNullable(em.find(Tag.class,id));
    }

    public Optional<Tag> findByName(String name){
        return em.createQuery("select t from Tag t where t.name= :name",Tag.class)
                .setParameter("name",name)
                .getResultList().stream().findAny();
    }
}
