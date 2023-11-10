package com.ruoyi.system.repository.impl;


import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.system.domain.QSysRoleMenu;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.domain.idclazz.SysRoleMenuId;
import com.ruoyi.system.repository.SysRoleMenuRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SysRoleMenuRepositoryImpl extends BaseRepositoryImpl<SysRoleMenu, SysRoleMenuId> implements SysRoleMenuRepository {
    public SysRoleMenuRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysRoleMenu.class, em, factory);
    }
    final QSysRoleMenu rm = QSysRoleMenu.sysRoleMenu;
    @Override
    public long checkMenuExistRole(Long menuId) {
        blazeJPAQueryFactory.selectFrom(rm).where(
                rm.menuId.eq(menuId)
        ).fetchCount();
        return 0;
    }

    @Override
    public long deleteRoleMenuByRoleId(Long roleId) {
        return blazeJPAQueryFactory.delete(rm).where(
                rm.roleId.eq(roleId)
        ).execute();

    }

    @Override
    public long deleteRoleMenu(Long[] ids) {
        return blazeJPAQueryFactory.delete(rm).where(
                rm.roleId.in(ids)
        ).execute();
    }
}
