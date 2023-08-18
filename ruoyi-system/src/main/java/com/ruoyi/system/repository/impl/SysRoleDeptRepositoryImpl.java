package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.system.domain.QSysRoleDept;
import com.ruoyi.system.domain.SysRoleDept;
import com.ruoyi.system.domain.idclazz.SysRoleDeptId;
import com.ruoyi.system.repository.SysRoleDeptRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysRoleDeptRepositoryImpl extends BaseRepositoryImpl<SysRoleDept, SysRoleDeptId> implements SysRoleDeptRepository {
    public SysRoleDeptRepositoryImpl( EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysRoleDept.class, em, factory);
    }

    final QSysRoleDept rd = QSysRoleDept.sysRoleDept;

    @Override
    public long deleteRoleDeptByRoleId(Long roleId) {
        return blazeJPAQueryFactory.delete(rd).where(rd.roleId.eq(roleId)).execute();
    }

    @Override
    public long deleteRoleDept(Long[] ids) {
        return blazeJPAQueryFactory.delete(rd).where(rd.roleId.in(ids)).execute();
    }

    @Override
    public long selectCountRoleDeptByDeptId(Long deptId) {
        return blazeJPAQueryFactory.selectFrom(rd).where(rd.deptId.eq(deptId)).fetchCount();
    }



}
