package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.domain.entity.QSysUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.common.utils.UpdateBooleanBuilder;
import com.ruoyi.system.domain.QSysPost;
import com.ruoyi.system.domain.QSysUserPost;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.repository.SysPostRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class SysPostRepositoryImpl extends BaseRepositoryImpl<SysPost,Long> implements SysPostRepository {
    public SysPostRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysPost.class, em, factory);
    }
    final QSysPost p = QSysPost.sysPost;
    final QSysUserPost up = QSysUserPost.sysUserPost;
    final QSysUser u = QSysUser.sysUser;

    @Override
    public List<SysPost> selectPostList(SysPost post, PageDomain pageDomain) {
        BlazeJPAQuery<SysPost> jpaQuery = blazeJPAQueryFactory.selectFrom(p)
                .where(
                        SelectBooleanBuilder.builder()
                                .notEmptyLike(post.getPostCode(), p.postCode)
                                .notEmptyEq(post.getStatus(), p.status)
                                .notEmptyLike(post.getPostName(), p.postName)
                                .build()
                ).orderBy(p.postId.asc());
        return this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);
    }

    @Override
    public int updatePost(SysPost post) {
        return (int) UpdateBooleanBuilder.builder(blazeJPAQueryFactory.update(p))
                .notEmptySet(post.getPostCode(),p.postCode)
                .notEmptySet(post.getPostName(),p.postName)
                .notEmptySet(post.getPostSort(),p.postSort)
                .notEmptySet(post.getStatus(),p.status)
                .notEmptySet(post.getRemark(),p.remark)
                .notEmptySet(post.getUpdateBy(),p.updateBy)
                .notEmptySet(new Date(),p.updateTime)
                .build(p.postId.eq(post.getPostId())).execute();
    }

    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        return blazeJPAQueryFactory.select(p.postId).from(p)
                .leftJoin(up).on(up.postId.eq(p.postId))
                .leftJoin(u).on(u.userId.eq(up.userId))
                .where(
                        u.userId.eq(userId)
                ).fetch();

    }

    @Override
    public List<SysPost> selectPostsByUserName(String userName) {
        return blazeJPAQueryFactory.selectFrom(p)
                .leftJoin(up).on(up.postId.eq(p.postId))
                .leftJoin(u).on(u.userId.eq(up.userId))
                .where(
                        u.userName.eq(userName)
                ).fetch();
    }

    @Override
    public long deletePostByIds(Long[] postIds) {
        return blazeJPAQueryFactory.delete(p).where(p.postId.in(postIds)).execute();
    }

    @Override
    public SysPost checkPostNameUnique(String postName) {
        return blazeJPAQueryFactory.selectFrom(p).where(
                p.postName.eq(postName)
        ).fetchOne();
    }

    @Override
    public SysPost checkPostCodeUnique(String postCode) {
        return blazeJPAQueryFactory.selectFrom(p).where(
                p.postCode.eq(postCode)
        ).fetchOne();
    }
}
