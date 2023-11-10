package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.common.utils.UpdateBooleanBuilder;
import com.ruoyi.system.domain.QSysNotice;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.repository.SysNoticeRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Repository
@Transactional
public class SysNoticeRepositoryImpl extends BaseRepositoryImpl<SysNotice,Long> implements SysNoticeRepository {
    public SysNoticeRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysNotice.class, em, factory);
    }

    final QSysNotice n = QSysNotice.sysNotice;

    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice, PageDomain pageDomain) {
        BlazeJPAQuery<SysNotice> jpaQuery = blazeJPAQueryFactory.selectFrom(n).where(
                SelectBooleanBuilder.builder()
                        .notEmptyLike(notice.getNoticeTitle(), n.noticeTitle)
                        .notEmptyEq(notice.getNoticeType(), n.noticeType)
                        .notEmptyLike(notice.getCreateBy(), n.createBy)
                        .build()
        ).orderBy(n.noticeId.desc());
        return this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);

    }


    @Override
    public long deleteNoticeByIds(Long[] noticeIds) {
        return blazeJPAQueryFactory.delete(n).where(
                n.noticeId.in(noticeIds)
        ).execute();
    }

    @Override
    public int updateNotice(SysNotice notice) {
        return (int) UpdateBooleanBuilder.builder(blazeJPAQueryFactory.update(n))
                .notEmptySet(notice.getNoticeTitle(),n.noticeTitle)
                .notEmptySet(notice.getNoticeType(),n.noticeType)
                .notEmptySet(notice.getNoticeContent(),n.noticeContent)
                .notEmptySet(notice.getStatus(),n.status)
                .notEmptySet(notice.getUpdateBy(),n.updateBy)
                .notEmptySet(new Date(),n.updateTime)
                .build(n.noticeId.eq(notice.getNoticeId())).execute();
    }
}
