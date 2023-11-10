package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.domain.entity.QSysDept;
import com.ruoyi.common.core.domain.entity.QSysRole;
import com.ruoyi.common.core.domain.entity.QSysUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.common.utils.UpdateBooleanBuilder;
import com.ruoyi.system.domain.QSysUserRole;
import com.ruoyi.system.repository.SysUserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;

@Repository
@Transactional
public class SysUserRepositoryImpl extends BaseRepositoryImpl<SysUser,Long> implements SysUserRepository {
    public SysUserRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysUser.class, em, factory);
    }
    final QSysUser u = QSysUser.sysUser;
    final QSysDept d = QSysDept.sysDept;
    final QSysRole r = QSysRole.sysRole;
    final QSysUserRole ur = QSysUserRole.sysUserRole;

    @Override
    public List<SysUser> selectUserList(SysUser sysUser, PageDomain pageDomain) {
        QSysDept sd = QSysDept.sysDept;
        BlazeJPAQuery<SysUser> jpaQuery = blazeJPAQueryFactory.selectFrom(u)
                .where(
                        SelectBooleanBuilder.builder()
                                .notEmptyEq("0", u.delFlag)
                                .notEmptyLike(sysUser.getUserName(), u.userName)
                                .notEmptyEq(sysUser.getStatus(), u.status)
                                .notEmptyLike(sysUser.getPhonenumber(), u.phonenumber)
                                .notEmptyDateAfter((String) sysUser.getParams().get("beginTime"), u.createTime)
                                .notEmptyDateBefter((String) sysUser.getParams().get("endTime"), u.createTime, () -> LocalTime.of(23, 59, 59))
                                .notEmptyExpressions(sysUser.getParams())
                                .notEmptyEqOrIn(sysUser.getDeptId(),u.deptId
                                        ,JPAExpressions.select(sd.deptId).from(sd).where(Expressions.booleanTemplate("function('FIND_IN_SET',{0},{1})>0",sysUser.getDeptId(),sd.ancestors)))
                                .build()
                ).orderBy(u.userId.asc());

        return this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);
    }

    @Override
    public List<SysUser> selectAllocatedList(SysUser user,PageDomain pageDomain) {
        BlazeJPAQuery<SysUser> jpaQuery = blazeJPAQueryFactory.selectDistinct(u).from(u)
                .leftJoin(d).on(u.deptId.eq(d.deptId))
                .leftJoin(ur).on(u.userId.eq(ur.userId))
                .leftJoin(r).on(r.roleId.eq(ur.roleId))
                .where(
                        u.delFlag.eq("0"), r.roleId.eq(user.getRoleId()),
                        SelectBooleanBuilder.builder()
                                .notEmptyLike(user.getUserName(), u.userName)
                                .notEmptyLike(user.getPhonenumber(), u.phonenumber)
                                .notEmptyExpressions(user.getParams())
                                .build()
                ).orderBy(u.userId.asc());
        return this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);

    }

    @Override
    public List<SysUser> selectUnallocatedList(SysUser user,PageDomain pageDomain) {
        QSysUser subU = new QSysUser("subu");
        QSysUserRole subUr = new QSysUserRole("subUr");
        BlazeJPAQuery<SysUser> jpaQuery = blazeJPAQueryFactory.selectDistinct(u).from(u)
                .leftJoin(d).on(u.deptId.eq(d.deptId))
                .leftJoin(ur).on(u.userId.eq(ur.userId))
                .leftJoin(r).on(r.roleId.eq(ur.roleId))
                .where(
                        u.delFlag.eq("0").and(r.roleId.ne(user.getRoleId()).or(r.roleId.isNull()))
                        , u.userId.notIn(
                                JPAExpressions.select(subU.userId)
                                        .from(subU)
                                        .innerJoin(subUr)
                                        .on(subU.userId.eq(subUr.userId), subUr.roleId.eq(user.getRoleId()))
                        )
                        , SelectBooleanBuilder.builder()
                                .notEmptyLike(user.getUserName(), u.userName)
                                .notEmptyLike(user.getPhonenumber(), u.phonenumber)
                                .notEmptyExpressions(user.getParams())
                                .build()
                ).orderBy(u.userId.asc());
        return this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);
    }

    @Override
    public SysUser selectUserByUserName(String userName) {
        Map<String, Group> transform = blazeJPAQueryFactory.from(u)
                .leftJoin(d).on(u.deptId.eq(d.deptId))
                .leftJoin(ur).on(u.userId.eq(ur.userId))
                .leftJoin(r).on(r.roleId.eq(ur.roleId))
                .where(u.userName.eq(userName), u.delFlag.eq("0"))
                .transform(GroupBy.groupBy(u.userName).as( u,d, GroupBy.list(r)));
        if (!transform.isEmpty()){
            Group group = transform.get(userName);
            SysUser user = group.getOne(u);
            user.setDept(group.getOne(d));
            user.setRoles(group.getList(r));
            return user;
        }
        return null;


    }

    @Override
    public SysUser selectUserById(Long userId) {
        Map<Long, Group> transform = blazeJPAQueryFactory.from(u)
                .leftJoin(d).on(u.deptId.eq(d.deptId))
                .leftJoin(ur).on(u.userId.eq(ur.userId))
                .leftJoin(r).on(r.roleId.eq(ur.roleId))
                .where(u.userId.eq(userId))
                .transform(GroupBy.groupBy(u.userId).as( u,d, GroupBy.list(r)));
        if (!transform.isEmpty()){
            Group group = transform.get(userId);
            SysUser user = group.getOne(u);
            user.setDept(group.getOne(d));
            user.setRoles(group.getList(r));
            return user;
        }
        return null;
    }




    @Override
    public int updateUserAvatar(String userName, String avatar) {
        blazeJPAQueryFactory.update(u).set(u.avatar,avatar)
                .where(u.userName.eq(userName));
        return 1;
    }

    @Override
    public long resetUserPwd(String userName, String password) {
        return blazeJPAQueryFactory.update(u).set(u.password,password)
                .where(u.userName.eq(userName)).execute();
    }

    @Override
    public long deleteUserById(Long userId) {
        return blazeJPAQueryFactory.update(u).set(u.delFlag,"2").where(u.userId.eq(userId)).execute();
    }

    @Override
    public long deleteUserByIds(Long[] userIds) {
        return blazeJPAQueryFactory.update(u).set(u.delFlag,"2")
                .where(u.userId.in(userIds)).execute();
    }

    @Override
    public SysUser checkUserNameUnique(String userName) {
        return blazeJPAQueryFactory.selectFrom(u).where(u.userName.eq(userName),u.delFlag.eq("0")).fetchOne();
    }

    @Override
    public SysUser checkPhoneUnique(String phonenumber) {
        return blazeJPAQueryFactory.selectFrom(u).where(u.phonenumber.eq(phonenumber),u.delFlag.eq("0")).fetchOne();
    }

    @Override
    public SysUser checkEmailUnique(String email) {
       return blazeJPAQueryFactory.selectFrom(u).where(u.email.eq(email),(u.delFlag.eq("0"))).fetchOne();
    }

    @Override
    public int updateUser(SysUser user) {
        return (int) UpdateBooleanBuilder.builder(blazeJPAQueryFactory.update(u))
                .notEmptySet(user.getDeptId(), u.deptId)
                .notEmptySet(user.getUserName(), u.userName)
                .notEmptySet(user.getNickName(), u.nickName)
                .notEmptySet(user.getEmail(), u.email)
                .notEmptySet(user.getPhonenumber(), u.phonenumber)
                .notEmptySet(user.getSex(), u.sex)
                .notEmptySet(user.getAvatar(), u.avatar)
                .notEmptySet(user.getPassword(), u.password)
                .notEmptySet(user.getStatus(), u.status)
                .notEmptySet(user.getLoginIp(), u.loginIp)
                .notEmptySet(user.getLoginDate(), u.loginDate)
                .notEmptySet(user.getUpdateBy(), u.updateBy)
                .notEmptySet(user.getRemark(), u.remark)
                .notEmptySet(new Date(),u.updateTime)
                .build(u.userId.eq(user.getUserId())).execute();
    }
}
