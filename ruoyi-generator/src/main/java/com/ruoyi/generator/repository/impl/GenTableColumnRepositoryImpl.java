package com.ruoyi.generator.repository.impl;


import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.utils.UpdateBooleanBuilder;
import com.ruoyi.generator.domain.GenTableColumn;
import com.ruoyi.generator.domain.QGenTableColumn;

import com.ruoyi.generator.repository.GenTableColumnRepository;
import jakarta.persistence.EntityManager;

import jakarta.persistence.Tuple;

import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

import java.util.stream.Collectors;

@Repository
public class GenTableColumnRepositoryImpl extends BaseRepositoryImpl<GenTableColumn,Long> implements GenTableColumnRepository {
    public GenTableColumnRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(GenTableColumn.class, em, factory);
    }

    final QGenTableColumn c = QGenTableColumn.genTableColumn;

    @Override
    public List<GenTableColumn> selectDbTableColumnsByName(String tableName) {
        String sql = """
                		select column_name as columnName
                		, (case when (is_nullable = 'no' && column_key != 'PRI') then '1' else null end) as isRequired
                		, (case when column_key = 'PRI' then '1' else '0' end)  as isPk
                		, ordinal_position as sort
                		, column_comment as columnComment
                		, (case when extra = 'auto_increment' then '1' else '0' end)  as isIncrement
                		, column_type  as columnType
                		from information_schema.columns where table_schema = (select database()) and table_name = ?1 order by ordinal_position
                """;
        List<Tuple> resultList = this.nativeQuery(sql,Tuple.class).setParameter(1, tableName).getResultList();
        List<GenTableColumn> collect = resultList.stream().map(tuple -> {
            GenTableColumn tableColumn = new GenTableColumn();
            tableColumn.setColumnName((String) tuple.get("columnName"));
            tableColumn.setIsRequired((Character) tuple.get("isRequired"));
            tableColumn.setIsPk(((Character) tuple.get("isPk")));
            tableColumn.setSort(Math.toIntExact((Long) tuple.get("sort")));
            tableColumn.setColumnComment((String) tuple.get("columnComment"));
            tableColumn.setIsIncrement((Character) tuple.get("isIncrement"));
            tableColumn.setColumnType((String) tuple.get("columnType"));
            return tableColumn;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId) {
        return blazeJPAQueryFactory.selectFrom(c)
                .where(c.tableId.eq(tableId)).orderBy(c.sort.asc()).fetch();
    }

    @Override
    public int updateGenTableColumn(GenTableColumn tableColumn) {
        return Math.toIntExact(UpdateBooleanBuilder.builder(blazeJPAQueryFactory.update(c))
                .notEmptySet(tableColumn.getColumnComment(), c.columnComment)
                .notEmptySet(tableColumn.getJavaType(), c.javaType)
                .notEmptySet(tableColumn.getJavaField(), c.javaField)
                .notEmptySet(tableColumn.getIsInsert(), c.isInsert)
                .notEmptySet(tableColumn.getIsEdit(), c.isEdit)
                .notEmptySet(tableColumn.getIsList(), c.isList)
                .notEmptySet(tableColumn.getIsQuery(), c.isQuery)
                .notEmptySet(tableColumn.getIsRequired(), c.isRequired)
                .notEmptySet(tableColumn.getQueryType(), c.queryType)
                .notEmptySet(tableColumn.getHtmlType(), c.htmlType)
                .notEmptySet(tableColumn.getDictType(), c.dictType)
                .notEmptySet(tableColumn.getSort(), c.sort)
                .notEmptySet(tableColumn.getUpdateBy(), c.updateBy)
                .notEmptySet(new Date(), c.updateTime)
                .build(c.columnId.eq(tableColumn.getColumnId()))
                .execute());

    }

    @Override
    public int deleteGenTableColumnByIds(Long[] ids) {
        return (int) blazeJPAQueryFactory.delete(c)
                .where(c.tableId.in(ids)).execute();
    }


}
