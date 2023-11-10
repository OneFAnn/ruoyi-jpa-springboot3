package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.dml.UpdateClause;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.domain.entity.QSysDictData;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.common.utils.UpdateBooleanBuilder;
import com.ruoyi.system.repository.SysDictDataRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class SysDictDataRepositoryImpl extends BaseRepositoryImpl<SysDictData,Long> implements SysDictDataRepository {

    final QSysDictData qSysDictData = QSysDictData.sysDictData;

    public SysDictDataRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysDictData.class, em, factory);
    }


    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData, PageDomain pageDomain) {
        BlazeJPAQuery<SysDictData> jpaQuery = blazeJPAQueryFactory.selectFrom(qSysDictData)
                .where(SelectBooleanBuilder.builder()
                        .notEmptyEq(dictData.getDictType(), qSysDictData.dictType)
                        .notEmptyLike(dictData.getDictLabel(), qSysDictData.dictLabel)
                        .notEmptyEq(dictData.getStatus(), qSysDictData.status)
                        .build()
                )
                .orderBy(qSysDictData.dictCode.asc());
        return this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);

    }

    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        return blazeJPAQueryFactory.selectFrom(qSysDictData)
                .where(qSysDictData.dictType.eq(dictType),qSysDictData.status.eq("0"))
                .orderBy(qSysDictData.dictSort.asc()).fetch();
    }

    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return blazeJPAQueryFactory.select(qSysDictData.dictLabel).from(qSysDictData)
                .where(qSysDictData.dictType.eq(dictType),qSysDictData.dictValue.eq(dictType))
                .fetchOne();
    }



    @Override
    public long countDictDataByType(String dictType) {
        return blazeJPAQueryFactory.selectFrom(qSysDictData)
                .where(qSysDictData.dictType.eq(dictType)).fetchCount();
    }





    @Override
    public long updateDictDataType(String oldDictType, String newDictType) {
        return blazeJPAQueryFactory.update(qSysDictData).set(qSysDictData.dictType, newDictType)
                .where(qSysDictData.dictType.eq(oldDictType)).execute();
    }

    @Override
    public int updateDictData(SysDictData dictData) {
        UpdateClause<?> update = blazeJPAQueryFactory.update(qSysDictData);
        return (int) UpdateBooleanBuilder.builder(update)
                .notEmptySet(dictData.getDictSort(),qSysDictData.dictSort)
                .notEmptySet(dictData.getDictLabel(),qSysDictData.dictLabel)
                .notEmptySet(dictData.getDictValue(),qSysDictData.dictValue)
                .notEmptySet(dictData.getDictType(),qSysDictData.dictType)
                .notEmptySet(dictData.getCssClass(),qSysDictData.cssClass)
                .notEmptySet(dictData.getListClass(),qSysDictData.listClass)
                .notEmptySet(dictData.getIsDefault(),qSysDictData.isDefault)
                .notEmptySet(dictData.getStatus(),qSysDictData.status)
                .notEmptySet(dictData.getRemark(),qSysDictData.remark)
                .notEmptySet(dictData.getUpdateBy(),qSysDictData.updateBy)
                .notEmptySet(new Date(),qSysDictData.updateTime)
                .build(qSysDictData.dictCode.eq(dictData.getDictCode())).execute();
    }
}
