package com.bookpie.shop.repository;


import com.bookpie.shop.domain.QBookTag;
import com.bookpie.shop.domain.QTag;
import com.bookpie.shop.domain.QUsedBook;
import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.dto.FindUsedBookDto;
import com.bookpie.shop.enums.Category;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsedBookRepository {

    private final EntityManager em;

    public Long save(UsedBook usedBook){
        em.persist(usedBook);
        return usedBook.getId();
    }
    public Optional<UsedBook> findById(Long id){
        return Optional.ofNullable(em.find(UsedBook.class,id));
    }
    public boolean delete(Long id){
        UsedBook usedBook = findById(id).orElseThrow(()->new UsernameNotFoundException("등록된 책이 없습니다."));
        em.remove(usedBook);
        return true;
    }
    public List<UsedBook> findByIsbn(String isbn){
        return em.createQuery("select ub from UsedBook ub" +
                                    " where ub.isbn= :isbn")
                .setParameter("isbn",isbn)
                .getResultList();
    }


    public Optional<UsedBook> findByIdWithUser(Long id){
        return em.createQuery("select ub from UsedBook  ub" +
                                " join fetch  ub.seller s" +
                                " where ub.id= :id")
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                 .setParameter("id",id).getResultList().stream().findAny();
    }

    public List<UsedBook> findByUserId(Long userId){
        return em.createQuery("select ub from UsedBook  ub" +
                                " join fetch ub.seller s" +
                                " where s.id= : userId",UsedBook.class)
                .setParameter("userId",userId)
                .getResultList();
    }

    public Optional<UsedBook> findByIdDetail(Long id){
        return em.createQuery("select distinct ub from UsedBook ub" +
                                    " join fetch ub.seller s" +
                                    " left outer join fetch ub.tags bt" +
                                    " left outer join fetch bt.tag t" +
                                    " where ub.id= :id",UsedBook.class)
                .setParameter("id",id)
                .getResultList().stream().findAny();
    }


    public List<UsedBook> findRelated(Category category,List<Long> tags){
        QUsedBook qUsedBook = QUsedBook.usedBook;
        QBookTag qBookTag = QBookTag.bookTag;
        QTag qTag = QTag.tag;
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.selectDistinct(qUsedBook)
                .from(qUsedBook)
                .leftJoin(qUsedBook.tags,qBookTag)
                .fetchJoin()
                .leftJoin(qBookTag.tag, qTag)
                .fetchJoin()
                .where(eqFstCategory(category),containTag(tags))
                .fetch();
    }
    public BooleanExpression containTag(List<Long> tagIds){
        if (tagIds.isEmpty()){
            return null;
        }else{
            return QTag.tag.id.in(tagIds);
        }
    }

    public List<UsedBook> findAll(FindUsedBookDto dto){
        QUsedBook qUsedBook= QUsedBook.usedBook;
        JPAQueryFactory query = new JPAQueryFactory(em);
        OrderSpecifier condition = null;
        if(dto.getSort().equals("date")){
            condition = qUsedBook.modifiedDate.desc();
        }else if (dto.getSort().equals("view")){
            condition = qUsedBook.view.desc();
        }
        return query.select(qUsedBook)
                .from(qUsedBook)
                .where(eqTitle(dto.getTitle()),
                        eqFstCategory(dto.getFstCategory()),
                        eqSndCategory(dto.getSndCategory()))
                .offset(dto.getOffset())
                .limit(dto.getLimit())
                .orderBy(condition)
                .fetch();
    }
    public Long count(FindUsedBookDto dto){
        QUsedBook qUsedBook = QUsedBook.usedBook;
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(qUsedBook.count())
                .from(qUsedBook)
                .where(eqTitle(dto.getTitle()),
                        eqFstCategory(dto.getFstCategory()),
                        eqSndCategory(dto.getSndCategory()))
                .fetchOne();
    }


    public Long count(Long userId){
        QUsedBook qUsedBook = QUsedBook.usedBook;
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(qUsedBook.count())
                          .from(qUsedBook)
                          .where(qUsedBook.seller.id.eq(userId))
                          .fetchOne();
    }

    public List<UsedBook> findAllByUserId(Long id,int offset,int limit){
        QUsedBook qUsedBook = QUsedBook.usedBook;
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(qUsedBook)
                .from(qUsedBook)
                .where(qUsedBook.seller.id.eq(id))
                .orderBy(qUsedBook.modifiedDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    public List<Tuple> groupCount(){
        QUsedBook qUsedBook = QUsedBook.usedBook;
        JPAQueryFactory query = new JPAQueryFactory(em);
        return query.select(qUsedBook.saleState,qUsedBook.count())
                .from(qUsedBook)
                .groupBy(qUsedBook.saleState)
                .fetch();
    }

    private BooleanExpression eqTitle(String title){
        if(!StringUtils.hasText(title)){
            return null;
        }
        return QUsedBook.usedBook.title.containsIgnoreCase(title);

    }
    private BooleanExpression eqFstCategory(Category category){
        if(category == null){
            return null;
        }
        return QUsedBook.usedBook.fstCategory.eq(category);
    }

    private BooleanExpression eqSndCategory(Category category){
        if(category == null){
            return null;
        }
        return QUsedBook.usedBook.sndCategory.eq(category);
    }

    public List<UsedBook> getLikedBook(Long userId){
        return em.createQuery("select ub from UsedBook ub" +
                        " join fetch ub.likes l"+
                        " join fetch l.user u" +
                        " where u.id=: userId",UsedBook.class)
                .setParameter("userId",userId)
                .getResultList();
    }


}
