package com.ruoyi.generator.repository.impl;

import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.generator.domain.GenTable;
import com.ruoyi.generator.domain.QGenTable;
import com.ruoyi.generator.domain.idclass.TablesViewId;
import com.ruoyi.generator.domain.views.QTables;
import com.ruoyi.generator.domain.views.Tables;
import com.ruoyi.generator.repository.TablesViewRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class TablesViewRepositoryImpl extends BaseRepositoryImpl<Tables, TablesViewId> implements TablesViewRepository {
    public TablesViewRepositoryImpl(EntityManager em, BlazeJPAQueryFactory jpaQueryFactory) {
        super(Tables.class, em, jpaQueryFactory);
    }
    final QTables tv = QTables.tables;

    @Override
    public List<GenTable> selectDbTableList(GenTable genTable, PageDomain pageDomain) {
        QGenTable gt = QGenTable.genTable;
        BlazeJPAQuery<GenTable> jpaQuery = blazeJPAQueryFactory.select(
                Projections.bean(GenTable.class,tv.tableName,tv.tableComment,tv.tableSchema,tv.createTime,tv.updateTime)
        ).from(tv).where(
                SelectBooleanBuilder.builder()
                        .notEmptyEq("ry-vue",tv.tableSchema)
                        .notEmptyNotLike("qrtz_",tv.tableName)
                        .notEmptyNotLike("gen_",tv.tableName)
                        .notEmptyExpressions(tv.tableName.notIn(JPAExpressions.select(gt.tableName).from(gt)))
                        .notEmptyEq(genTable.getTableName(), tv.tableName)
                        .notEmptyEq(genTable.getTableComment(), tv.tableComment)
                        .notEmptyDateAfter((String) genTable.getParams().get("beginTime"), tv.createTime)
                        .notEmptyDateBefore((String) genTable.getParams().get("endTime"), tv.createTime, () -> LocalTime.of(23, 59, 59))
                        .build()
        ).orderBy(tv.createTime.desc(),tv.tableName.desc(),tv.tableSchema.desc());
        return this.fetchPage(jpaQuery, pageDomain).orElseGet(jpaQuery::fetch);


    }

    @Override
    public List<Tables> selectDbTableListByNames(String[] tableNames) {
        return blazeJPAQueryFactory.selectFrom(tv).where(
                SelectBooleanBuilder.builder()
                        .notEmptyEq("ry-vue",tv.tableSchema)
                        .notEmptyNotLike("qrtz_",tv.tableName)
                        .notEmptyNotLike("gen_",tv.tableName)
                        .notEmptyIn(tableNames,tv.tableName)
                        .build()
        ).orderBy(tv.createTime.desc()).fetch();

    }

}
