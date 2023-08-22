package com.ruoyi.common.core;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.types.OrderSpecifier;
import com.ruoyi.common.core.page.PageDomain;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepositoryImpl<T,ID> extends SimpleJpaRepository<T,ID> implements BaseRepository<T,ID> {

    private final EntityManager em;
    protected final BlazeJPAQueryFactory blazeJPAQueryFactory;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em, BlazeJPAQueryFactory jpaQueryFactory) {
        super(domainClass, em);
        this.em = em;
        this.blazeJPAQueryFactory = jpaQueryFactory;
//        this.blazeJPAQueryFactory = new BlazeJPAQueryFactory(em,criteriaBuilderFactory);
    }

    @Override
    public void clear() {
        em.clear();
    }

    @Override
    public void detach(T entity) {
        em.detach(entity);
    }

    public Query nativeQuery(String sql, Class clazz){
        return em.createNativeQuery(sql, clazz);
    }

    public Query nativeQuery(String sql){
        return em.createNativeQuery(sql);
    }

    @Override
    public <K> Optional<List<K>> fetchPage(BlazeJPAQuery<K> jpaQuery,PageDomain pageDomain){
        return Optional.ofNullable(pageDomain).map(page -> {
            Optional<OrderSpecifier> dslOrderBy = pageDomain.getDslOrderBy();
            dslOrderBy.map(orderSpecifier -> jpaQuery.orderBy(orderSpecifier));
            return jpaQuery.fetchPage(pageDomain.offset(), pageDomain.getPageSize());
        });
    }
}
