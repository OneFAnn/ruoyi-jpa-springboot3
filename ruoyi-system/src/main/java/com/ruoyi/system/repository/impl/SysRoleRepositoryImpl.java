package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.common.utils.UpdateBooleanBuilder;
import com.ruoyi.system.domain.QSysUserRole;
import com.ruoyi.system.repository.SysRoleRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SysRoleRepositoryImpl extends BaseRepositoryImpl<SysRole,Long> implements SysRoleRepository {
    public SysRoleRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysRole.class, em, factory);
    }

    final QSysRole r  = QSysRole.sysRole;
    final QSysUserRole ur = QSysUserRole.sysUserRole;
    final QSysUser u = QSysUser.sysUser;
    final QSysDept d = QSysDept.sysDept;

    @Override
    public int updateRole(SysRole role) {
        return (int) UpdateBooleanBuilder.builder(blazeJPAQueryFactory.update(r))
                .notEmptySet(role.getRoleName(), r.roleName)
                .notEmptySet(role.getRoleKey(), r.roleKey)
                .notEmptySet(role.getRoleSort(), r.roleSort)
                .notEmptySet(role.getDataScope(), r.dataScope)
                .notEmptySet(role.isMenuCheckStrictly(), r.menuCheckStrictly)
                .notEmptySet(role.isDeptCheckStrictly(), r.deptCheckStrictly)
                .notEmptySet(role.getStatus(), r.status)
                .notEmptySet(role.getRemark(), r.remark)
                .notEmptySet(role.getUpdateBy(), r.updateBy)
                .notEmptySet(new Date(),r.updateTime)
                .build(r.roleId.eq(role.getRoleId())).execute();
    }

    @Override
    public List<SysRole> selectRoleList(SysRole role, PageDomain pageDomain) {
        BlazeJPAQuery<SysRole> jpaQuery = blazeJPAQueryFactory.selectFrom(r).where(
                SelectBooleanBuilder.builder()
                        .notEmptyEq(role.getRoleId(), r.roleId)
                        .notEmptyLike(role.getRoleName(), r.roleName)
                        .notEmptyEq(role.getStatus(), r.status)
                        .notEmptyEq(role.getRoleKey(), r.roleKey)
                        .notEmptyDateAfter((String) role.getParams().get("beginTime"), r.createTime)
                        .notEmptyDateBefter((String) role.getParams().get("endTime"), r.createTime, () -> LocalTime.of(23, 59, 59))
                        .build()
        ).orderBy(r.roleId.asc());
        return  this.fetchPage(jpaQuery,pageDomain).orElseGet(jpaQuery::fetch);

    }

    @Override
    public List<SysRole> selectRolePermissionByUserId(Long userId) {
        return blazeJPAQueryFactory.selectDistinct(r).from(r)
                .leftJoin(ur).on(ur.roleId.eq(r.roleId))
                .leftJoin(u).on(u.userId.eq(ur.userId))
                .leftJoin(d).on(u.deptId.eq(d.deptId))
                .where(r.delFlag.eq("0"),ur.userId.eq(userId)).fetch();
    }

    @Override
    public List<SysRole> selectRoleAll() {
        return blazeJPAQueryFactory.selectDistinct(r).from(r)
                .leftJoin(ur).on(ur.roleId.eq(r.roleId))
                .leftJoin(u).on(u.userId.eq(ur.userId))
                .leftJoin(d).on(u.deptId.eq(d.deptId))
                .fetch();
    }

    @Override
    public List<Long> selectRoleListByUserId(Long userId) {
        return blazeJPAQueryFactory.select(r.roleId).from(r)
                .leftJoin(ur).on(ur.roleId.eq(r.roleId))
                .leftJoin(u).on(u.userId.eq(ur.userId))
                .where(u.userId.eq(userId))
                .fetch();

    }



    @Override
    public List<SysRole> selectRolesByUserName(String userName) {
        return blazeJPAQueryFactory.selectDistinct(r).from(r)
                .leftJoin(ur).on(ur.roleId.eq(r.roleId))
                .leftJoin(u).on(u.userId.eq(ur.userId))
                .leftJoin(d).on(u.deptId.eq(d.deptId))
                .where(r.delFlag.eq("0"),u.userName.eq(userName)).fetch();
    }

    @Override
    public SysRole checkRoleNameUnique(String roleName) {
        return  blazeJPAQueryFactory.selectFrom(r).where(r.roleName.eq(roleName),r.delFlag.eq("0")).fetchOne();

    }

    @Override
    public SysRole checkRoleKeyUnique(String roleKey) {
        return blazeJPAQueryFactory.selectFrom(r).where(r.roleKey.eq(roleKey),r.delFlag.eq("0")).fetchOne();
    }



    @Override
    public long deleteRoleById(Long roleId) {
        return blazeJPAQueryFactory.update(r).set(r.delFlag,"2").where(r.roleId.eq(roleId)).execute();
    }

    @Override
    public int deleteRoleByIds(Long[] roleIds) {
        saveAll(Arrays.stream(roleIds).map(id->{
            SysRole role = new SysRole();
            role.setRoleId(id);
            role.setDelFlag("2");
            return role;
        }).collect(Collectors.toList()));
        return 1;
    }
}
