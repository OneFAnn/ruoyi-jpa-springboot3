package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.system.domain.QSysUserPost;
import com.ruoyi.system.domain.SysUserPost;
import com.ruoyi.system.domain.idclazz.SysUserPostId;
import com.ruoyi.system.repository.SysUserPostRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class SysUserPostRepositoryImpl extends BaseRepositoryImpl<SysUserPost, SysUserPostId> implements SysUserPostRepository {
    public SysUserPostRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysUserPost.class, em, factory);
    }
    final QSysUserPost up = QSysUserPost.sysUserPost;
    @Override
    public long deleteUserPostByUserId(Long userId) {
        blazeJPAQueryFactory.delete(up).where(
                up.userId.eq(userId)
        ).execute();
        return 0;
    }

    @Override
    public long countUserPostById(Long postId) {
        return blazeJPAQueryFactory.selectFrom(up).fetchCount();
    }

    @Override
    public long deleteUserPost(Long[] ids) {
        return blazeJPAQueryFactory.delete(up)
                .where(up.userId.in(ids)).execute();
    }
}
