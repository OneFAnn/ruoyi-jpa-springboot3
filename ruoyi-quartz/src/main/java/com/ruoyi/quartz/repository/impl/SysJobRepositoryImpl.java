package com.ruoyi.quartz.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.quartz.domain.QSysJob;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.repository.SysJobRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysJobRepositoryImpl extends BaseRepositoryImpl<SysJob,Long> implements SysJobRepository {
    public SysJobRepositoryImpl( EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysJob.class, em, factory);
    }

    final QSysJob j = QSysJob.sysJob;

    @Override
    public List<SysJob> selectJobList(SysJob job, PageDomain pageDomain) {
        BlazeJPAQuery<SysJob> jpaQuery = blazeJPAQueryFactory.selectFrom(j)
                .where(
                        SelectBooleanBuilder.builder()
                                .notEmptyLike(job.getJobName(), j.jobName)
                                .notEmptyEq(job.getJobGroup(), j.jobGroup)
                                .notEmptyEq(job.getStatus(), j.status)
                                .notEmptyLike(job.getInvokeTarget(), j.invokeTarget)
                                .build()
                ).orderBy(j.jobId.desc());
        return this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);
    }


    @Override
    public long deleteJobByIds(Long[] ids) {
        return blazeJPAQueryFactory.delete(j)
                .where(j.jobId.in(ids)).execute();
    }
}
