package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.system.domain.QSysUserRole;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.idclazz.SysUserRoleId;
import com.ruoyi.system.repository.SysUserRoleRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Repository
@Transactional
public class SysUserRoleRepositoryImpl extends BaseRepositoryImpl<SysUserRole, SysUserRoleId> implements SysUserRoleRepository {
    public SysUserRoleRepositoryImpl( EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysUserRole.class, em, factory);
    }

    final QSysUserRole ur = QSysUserRole.sysUserRole;

    @Override
    public long deleteUserRoleByUserId(Long userId) {
        return blazeJPAQueryFactory.delete(ur).where(
                ur.userId.eq(userId)
        ).execute();
    }

    @Override
    public long deleteUserRole(Long[] ids) {
        return blazeJPAQueryFactory.delete(ur).where(ur.userId.in(Arrays.asList(ids))).execute();
    }

    @Override
    public long countUserRoleByRoleId(Long roleId) {
        return blazeJPAQueryFactory.selectFrom(ur).where(ur.roleId.eq(roleId)).fetchCount();
    }


    @Override
    public long deleteUserRoleInfo(SysUserRole userRole) {
        return blazeJPAQueryFactory.delete(ur).where(ur.userId.eq(userRole.getUserId()).and(ur.roleId.eq(userRole.getRoleId()))).execute();
    }

    @Override
    public long deleteUserRoleInfos(Long roleId, Long[] userIds) {
        return blazeJPAQueryFactory.delete(ur).where(ur.roleId.eq(roleId).and(ur.userId.in(userIds))).execute();
    }
}
