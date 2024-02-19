package com.ruoyi.quartz.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.quartz.domain.QSysJobLog;
import com.ruoyi.quartz.domain.SysJobLog;
import com.ruoyi.quartz.repository.SysJobLogRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public class SysJobLogRepositoryImpl extends BaseRepositoryImpl<SysJobLog,Long> implements SysJobLogRepository {
    public SysJobLogRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysJobLog.class, em, factory);
    }
    final QSysJobLog jl = QSysJobLog.sysJobLog;

    @Override
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog, PageDomain pageDomain) {
        BlazeJPAQuery<SysJobLog> jpaQuery = blazeJPAQueryFactory.selectFrom(jl)
                .where(
                        SelectBooleanBuilder.builder()
                                .notEmptyLike(jobLog.getJobName(), jl.jobName)
                                .notEmptyEq(jobLog.getJobGroup(), jl.jobGroup)
                                .notEmptyEq(jobLog.getStatus(), jl.status)
                                .notEmptyLike(jobLog.getInvokeTarget(), jl.invokeTarget)
                                .notEmptyDateAfter((String) jobLog.getParams().get("beginTime"), jl.createTime)
                                .notEmptyDateBefore((String) jobLog.getParams().get("endTime"), jl.createTime, () -> LocalTime.of(23, 59, 59))
                                .build()
                ).orderBy(jl.jobLogId.desc());
        return this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);

    }

    @Override
    public long deleteJobLogByIds(Long[] logIds) {
        return blazeJPAQueryFactory.delete(jl).where(
                jl.jobLogId.in(logIds)
        ).execute();
    }
}
