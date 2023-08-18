package com.ruoyi.generator.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.generator.domain.GenTableColumn;
import com.ruoyi.generator.domain.QGenTableColumn;
import com.ruoyi.generator.repository.GenTableColumnRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GenTableColumnRepositoryImpl extends BaseRepositoryImpl<GenTableColumn,Long> implements GenTableColumnRepository {
    public GenTableColumnRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(GenTableColumn.class, em, factory);
    }

    final QGenTableColumn c = QGenTableColumn.genTableColumn;

    @Override
    public List<GenTableColumn> selectDbTableColumnsByName(String tableName) {
        return null;
    }

    @Override
    public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId) {
        return blazeJPAQueryFactory.selectFrom(c)
                .where(c.tableId.eq(tableId)).orderBy(c.sort.asc()).fetch();
    }

    @Override
    public int deleteGenTableColumnByIds(Long[] ids) {
        return (int) blazeJPAQueryFactory.delete(c)
                .where(c.tableId.in(ids)).execute();
    }
}
