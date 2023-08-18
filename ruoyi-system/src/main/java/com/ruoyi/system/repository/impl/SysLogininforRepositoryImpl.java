package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.system.domain.QSysLogininfor;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.repository.SysLogininforRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public class SysLogininforRepositoryImpl extends BaseRepositoryImpl<SysLogininfor,Long> implements SysLogininforRepository {

    final QSysLogininfor qSysLogininfor = QSysLogininfor.sysLogininfor;

    public SysLogininforRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysLogininfor.class, em, factory);
    }


    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor, PageDomain pageDomain) {
        BlazeJPAQuery<SysLogininfor> jpaQuery = blazeJPAQueryFactory.selectFrom(qSysLogininfor)
                .where(
                        SelectBooleanBuilder.builder()
                                .notEmptyLike(logininfor.getIpaddr(), qSysLogininfor.ipaddr)
                                .notEmptyEq(logininfor.getStatus(), qSysLogininfor.status)
                                .notEmptyLike(logininfor.getUserName(), qSysLogininfor.userName)
                                .notEmptyDateAfter((String) logininfor.getParams().get("beginTime"), qSysLogininfor.createTime)
                                .notEmptyDateBefter((String) logininfor.getParams().get("endTime"), qSysLogininfor.createTime, () -> LocalTime.of(23, 59, 59))
                                .build()
                )
                .orderBy(qSysLogininfor.infoId.desc());
        return this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);

    }


}
