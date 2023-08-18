package com.ruoyi.common.core;

import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.types.OrderSpecifier;
import com.ruoyi.common.core.page.PageDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T,ID> extends JpaRepository<T,ID> {
    void clear();

    void detach(T entity);

    Optional<List<T>> fetchPage(BlazeJPAQuery<T> jpaQuery,PageDomain pageDomain);
}
