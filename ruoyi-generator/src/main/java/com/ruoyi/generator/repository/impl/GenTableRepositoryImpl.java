package com.ruoyi.generator.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupBy;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.generator.domain.GenTable;
import com.ruoyi.generator.domain.GenTableColumn;
import com.ruoyi.generator.domain.QGenTable;
import com.ruoyi.generator.domain.QGenTableColumn;
import com.ruoyi.generator.repository.GenTableRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GenTableRepositoryImpl extends BaseRepositoryImpl<GenTable,Long> implements GenTableRepository {
    public GenTableRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(GenTable.class, em, factory);
    }
    final QGenTable t = QGenTable.genTable;
    final QGenTableColumn c = QGenTableColumn.genTableColumn;


    @Override
    public List<GenTable> selectGenTableList(GenTable genTable, PageDomain pageDomain) {
        BlazeJPAQuery<GenTable> jpaQuery = blazeJPAQueryFactory.selectFrom(t)
                .where(
                        SelectBooleanBuilder.builder()
                                .notEmptyLike(genTable.getTableName(), t.tableName)
                                .notEmptyEq(genTable.getTableComment(), t.tableComment)
                                .notEmptyDateAfter((String) genTable.getParams().get("beginTime"), t.createTime)
                                .notEmptyDateBefter((String) genTable.getParams().get("endTime"), t.createTime, () -> LocalTime.of(23, 59, 59))
                                .build()
                ).orderBy(t.tableId.desc());
        return  this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);

    }

    @Override
    public List<GenTable> selectDbTableList(GenTable genTable) {
        return null;
    }

    @Override
    public List<GenTable> selectDbTableListByNames(String[] tableNames) {
        return null;
    }

    @Override
    public List<GenTable> selectGenTableAll() {
        Map<Long, Group> transform = blazeJPAQueryFactory.from(t)
                .leftJoin(c).on(t.tableId.eq(c.tableId))
                .orderBy(c.sort.asc())
                .transform(GroupBy.groupBy(t.tableId).as(t, GroupBy.list(c)));
        List<GenTable>  list = new ArrayList();
        transform.forEach((id,group)->{
            GenTable table = group.getOne(t);
            List<GenTableColumn> columns = group.getList(c);
            table.setColumns(columns);
            list.add(table);
        });
        return list;
    }

    @Override
    public GenTable selectGenTableById(Long id) {
        Map<Long, Group> transform = blazeJPAQueryFactory.from(t)
                .leftJoin(c).on(t.tableId.eq(c.tableId))
                .where(t.tableId.eq(id))
                .orderBy(c.sort.asc())
                .transform(GroupBy.groupBy(t.tableId).as(t, GroupBy.list(c)));
        if (!transform.isEmpty()){
            Group group = transform.get(id);
            GenTable genTable = group.getOne(t);
            genTable.setColumns(group.getList(c));
            return genTable;
        }
        return null;
    }

    @Override
    public GenTable selectGenTableByName(String tableName) {
        Map<Long, Group> transform = blazeJPAQueryFactory.from(t)
                .leftJoin(c).on(t.tableId.eq(c.tableId))
                .where(t.tableName.eq(tableName))
                .orderBy(c.sort.asc())
                .transform(GroupBy.groupBy(t.tableId).as(t, GroupBy.list(c)));
        Optional<GenTable> reuslt = Optional.empty();
        transform.forEach((k,group)->{
            GenTable genTable = group.getOne(t);
            genTable.setColumns(group.getList(c));
            reuslt.orElse(genTable);
        });
        return reuslt.get();
    }

    @Override
    public int deleteGenTableByIds(Long[] ids) {
        return (int) blazeJPAQueryFactory.delete(t)
                .where(t.tableId.in(ids)).execute();
    }
}
