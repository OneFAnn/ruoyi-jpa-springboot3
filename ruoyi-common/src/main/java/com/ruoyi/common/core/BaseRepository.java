package com.ruoyi.common.core;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.types.OrderSpecifier;
import com.ruoyi.common.core.page.PageDomain;
import jakarta.persistence.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T,ID> extends JpaRepository<T,ID> {
    void clear();

    void detach(T entity);

    <K> Optional<List<K>> fetchPage(BlazeJPAQuery<K> jpaQuery,PageDomain pageDomain);

    Query nativeQuery(String sql, Class clazz);
}
