package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.system.domain.QSysOperLog;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.repository.SysOperLogRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
@Repository
public class SysOperLogRepositoryImpl extends BaseRepositoryImpl<SysOperLog,Long> implements SysOperLogRepository {
    public SysOperLogRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysOperLog.class, em, factory);
    }

    final QSysOperLog op = QSysOperLog.sysOperLog;

    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog, PageDomain pageDomain) {
        BlazeJPAQuery<SysOperLog> jpaQuery = blazeJPAQueryFactory.selectFrom(op)
                .where(SelectBooleanBuilder.builder()
                        .notEmptyLike(operLog.getTitle(), op.title)
                        .notEmptyEq(operLog.getBusinessType(), op.businessType)
                        .notEmptyIn(operLog.getBusinessTypes(), op.businessType)
                        .notEmptyEq(operLog.getStatus(), op.status)
                        .notEmptyLike(operLog.getOperName(), op.operName)
                        .notEmptyDateAfter((String) operLog.getParams().get("beginTime"), op.createTime)
                        .notEmptyDateBefter((String) operLog.getParams().get("endTime"), op.createTime, () -> LocalTime.of(23, 59, 59))
                        .build()
                ).orderBy(op.operId.desc());
        return this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);
    }

    @Override
    public long deleteOperLogByIds(Long[] operIds) {
        return blazeJPAQueryFactory.delete(op).where(
                op.operId.in(operIds)
        ).execute();
    }


}
