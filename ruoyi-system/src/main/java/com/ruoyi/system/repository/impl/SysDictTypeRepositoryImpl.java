package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.domain.entity.QSysDictType;
import com.ruoyi.common.core.domain.entity.SysDictType;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.common.utils.UpdateBooleanBuilder;
import com.ruoyi.system.repository.SysDictTypeRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class SysDictTypeRepositoryImpl extends BaseRepositoryImpl<SysDictType,Long> implements SysDictTypeRepository {

    final QSysDictType qSysDictType = QSysDictType.sysDictType;

    public SysDictTypeRepositoryImpl( EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysDictType.class, em, factory);
    }

    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType, PageDomain pageDomain) {
        BlazeJPAQuery<SysDictType> jpaQuery = blazeJPAQueryFactory.selectFrom(qSysDictType)
                .where(
                        SelectBooleanBuilder.builder()
                                .notEmptyLike(dictType.getDictName(), qSysDictType.dictName)
                                .notEmptyEq(dictType.getStatus(), qSysDictType.status)
                                .notEmptyLike(dictType.getDictType(), qSysDictType.dictType)
                                .notEmptyDateAfter((String) dictType.getParams().get("beginTime"), qSysDictType.createTime)
                                .notEmptyDateBefore((String) dictType.getParams().get("endTime"), qSysDictType.createTime, () -> LocalTime.of(23, 59, 59))
                                .build()
                ).orderBy(qSysDictType.dictId.asc());
        return this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);

    }

    @Override
    public int updateDictType(SysDictType dictType) {
        return (int) UpdateBooleanBuilder.builder(blazeJPAQueryFactory.update(qSysDictType))
                .notEmptySet(dictType.getDictName(),qSysDictType.dictName)
                .notEmptySet(dictType.getDictType(),qSysDictType.dictType)
                .notEmptySet(dictType.getStatus(),qSysDictType.status)
                .notEmptySet(dictType.getRemark(),qSysDictType.remark)
                .notEmptySet(dictType.getUpdateBy(),qSysDictType.updateBy)
                .notEmptySet(new Date(),qSysDictType.updateTime)
                .build(qSysDictType.dictId.eq(dictType.getDictId())).execute();
    }


    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        return blazeJPAQueryFactory.selectFrom(qSysDictType).where(qSysDictType.dictType.eq(dictType)).fetchOne();
    }




    @Override
    public SysDictType checkDictTypeUnique(String dictType) {
        return blazeJPAQueryFactory.selectFrom(qSysDictType).where(qSysDictType.dictType.eq(dictType)).fetchOne();
    }
}
