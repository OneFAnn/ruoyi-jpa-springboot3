package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Coalesce;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.domain.entity.QSysMenu;
import com.ruoyi.common.core.domain.entity.QSysRole;
import com.ruoyi.common.core.domain.entity.QSysUser;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.common.utils.UpdateBooleanBuilder;
import com.ruoyi.system.domain.QSysRoleMenu;
import com.ruoyi.system.domain.QSysUserRole;
import com.ruoyi.system.repository.SysMenuRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysMenuRepositoryImpl extends BaseRepositoryImpl<SysMenu,Long> implements SysMenuRepository {
    public SysMenuRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysMenu.class, em, factory);
    }

    final QSysMenu qSysMenu = QSysMenu.sysMenu;
    final QSysRoleMenu qSysRoleMenu = QSysRoleMenu.sysRoleMenu;
    final QSysUserRole qSysUserRole = QSysUserRole.sysUserRole;
    final QSysRole qSysRole = QSysRole.sysRole;
    final QSysUser qSysUser = QSysUser.sysUser;

    @Override
    public List<SysMenu> selectMenuList(SysMenu menu) {
        return blazeJPAQueryFactory.selectFrom(qSysMenu)
                .where(
                        SelectBooleanBuilder.builder()
                                .notEmptyLike(menu.getMenuName(), qSysMenu.menuName)
                                .notEmptyEq(menu.getVisible(), qSysMenu.visible)
                                .notEmptyEq(menu.getStatus(), qSysMenu.status)
                                .build()
                ).fetch();



    }

    @Override
    public SysMenu selectMenuById(Long menuId) {
        return blazeJPAQueryFactory.select(
                Projections.bean(SysMenu.class,
                        qSysMenu.menuId,qSysMenu.parentId,
                        qSysMenu.menuName,qSysMenu.path,
                        qSysMenu.component,qSysMenu.query,
                        qSysMenu.visible,qSysMenu.status,
                        Expressions.asString(qSysMenu.perms.coalesce("")),qSysMenu.isFrame,
                        qSysMenu.isCache,qSysMenu.menuType,
                        qSysMenu.icon,qSysMenu.orderNum,qSysMenu.createTime)
        ).from(qSysMenu).where(
                qSysMenu.menuId.eq(menuId)
        ).fetchOne();
    }

    @Override
    public List<String> selectMenuPerms() {

        return blazeJPAQueryFactory.select(qSysMenu.perms)
                .from(qSysMenu)
                .leftJoin(qSysRoleMenu).on(qSysMenu.menuId.eq(qSysRoleMenu.menuId))
                .leftJoin(qSysUserRole).on(qSysRoleMenu.roleId.eq(qSysUserRole.roleId))
                .fetch();

    }

    @Override
    public List<SysMenu> selectMenuListByUserId(SysMenu menu) {
        return blazeJPAQueryFactory.selectFrom(qSysMenu)
                .leftJoin(qSysRoleMenu).on(qSysMenu.menuId.eq(qSysRoleMenu.menuId))
                .leftJoin(qSysUserRole).on(qSysRoleMenu.roleId.eq(qSysUserRole.roleId))
                .leftJoin(qSysRole).on(qSysUserRole.roleId.eq(qSysRole.roleId))
                .where(
                        SelectBooleanBuilder.builder()
                                .notEmptyEq((Long) menu.getParams().get("userId"),qSysUserRole.userId)
                                .notEmptyEq(menu.getMenuName(),qSysMenu.menuName)
                                .notEmptyEq(menu.getVisible(),qSysMenu.visible)
                                .build()
                ).fetch();
    }

    @Override
    public List<String> selectMenuPermsByRoleId(Long roleId) {
        return blazeJPAQueryFactory.selectDistinct(qSysMenu.perms)
                .from(qSysMenu)
                .leftJoin(qSysRoleMenu).on(qSysMenu.menuId.eq(qSysRoleMenu.menuId))
                .where(qSysMenu.status.eq("0"),qSysRoleMenu.roleId.eq(roleId)).fetch();
    }

    @Override
    public List<String> selectMenuPermsByUserId(Long userId) {
        return blazeJPAQueryFactory.selectDistinct(qSysMenu.perms)
                .from(qSysMenu)
                .leftJoin(qSysRoleMenu).on(qSysMenu.menuId.eq(qSysRoleMenu.menuId))
                .leftJoin(qSysUserRole).on(qSysRoleMenu.roleId.eq(qSysUserRole.roleId))
                .leftJoin(qSysRole).on(qSysRole.roleId.eq(qSysUserRole.roleId))
                .where(qSysMenu.status.eq("0"),qSysRole.status.eq("0"),qSysUserRole.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<SysMenu> selectMenuTreeAll() {
        return
                blazeJPAQueryFactory.selectDistinct(
                Projections.bean(SysMenu.class,
                        qSysMenu.menuId,qSysMenu.parentId,
                        qSysMenu.menuName,qSysMenu.path,
                        qSysMenu.component,qSysMenu.query,
                        qSysMenu.visible,qSysMenu.status,
                        qSysMenu.perms,qSysMenu.isFrame,
                        qSysMenu.isCache,qSysMenu.menuType,
                        qSysMenu.icon,qSysMenu.orderNum,qSysMenu.createTime)
                )
                .from(qSysMenu)
                .where(qSysMenu.menuType.in(List.of("M","C")),qSysMenu.status.eq("0"))
                .orderBy(qSysMenu.parentId.asc(),qSysMenu.orderNum.asc()).fetch();
    }

    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        return blazeJPAQueryFactory.selectDistinct(
                        Projections.bean(SysMenu.class,
                qSysMenu.menuId,qSysMenu.parentId,
                qSysMenu.menuName,qSysMenu.path,
                qSysMenu.component,qSysMenu.query,
                qSysMenu.visible,qSysMenu.status,
                                Expressions.asString(qSysMenu.perms.coalesce("")),qSysMenu.isFrame,
                qSysMenu.isCache,qSysMenu.menuType,
                qSysMenu.icon,qSysMenu.orderNum,qSysMenu.createTime)
        ).from(qSysMenu)
                .leftJoin(qSysRoleMenu).on(qSysMenu.menuId.eq(qSysRoleMenu.menuId))
                .leftJoin(qSysUserRole).on(qSysRoleMenu.roleId.eq(qSysUserRole.roleId))
                .leftJoin(qSysRole).on(qSysUserRole.roleId.eq(qSysRole.roleId))
                .leftJoin(qSysUser).on(qSysUserRole.userId.eq(qSysUser.userId))
                .where(qSysUser.userId.eq(userId),qSysMenu.menuType.in(List.of("M","C")),qSysMenu.status.eq("0"),qSysRole.status.eq("0"))
                .orderBy(qSysMenu.parentId.asc(),qSysMenu.orderNum.asc())
                .fetch();
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId, boolean menuCheckStrictly) {
        QSysMenu m = new QSysMenu("m");
        QSysRoleMenu rm = new QSysRoleMenu("rm");
        BlazeJPAQuery<Long> jpaQuery = blazeJPAQueryFactory.select(qSysMenu.menuId).from(qSysMenu)
                .leftJoin(qSysRoleMenu).on(qSysMenu.menuId.eq(qSysRoleMenu.menuId))
                .where(qSysRoleMenu.roleId.eq(roleId));
        if (menuCheckStrictly){
            jpaQuery.where(qSysMenu.menuId.notIn
                    (JPAExpressions.select(m.parentId)
                            .from(m).innerJoin(rm)
                            .on(m.menuId.eq(rm.menuId),rm.roleId.eq(roleId)))
            );
        }
        return jpaQuery.orderBy(qSysMenu.parentId.asc(),qSysMenu.orderNum.asc()).fetch();

    }


    @Override
    public long hasChildByMenuId(Long menuId) {
        return blazeJPAQueryFactory.selectFrom(qSysMenu).where(qSysMenu.parentId.eq(menuId)).fetchCount();
    }


    @Override
    public SysMenu checkMenuNameUnique(String menuName, Long parentId) {
        return blazeJPAQueryFactory.selectFrom(qSysMenu)
                .where(qSysMenu.menuName.eq(menuName),qSysMenu.parentId.eq(parentId))
                .fetchOne();
    }

    @Override
    public int updateMenu(SysMenu menu) {
        return (int) UpdateBooleanBuilder.builder(blazeJPAQueryFactory.update(qSysMenu))
                .notEmptySet(menu.getMenuName(),qSysMenu.menuName)
                .notEmptySet(menu.getParentId(),qSysMenu.parentId)
                .notEmptySet(menu.getOrderNum(),qSysMenu.orderNum)
                .notEmptySet(menu.getPath(),qSysMenu.path)
                .notEmptySet(menu.getComponent(),qSysMenu.component)
                .notEmptySet(menu.getQuery(),qSysMenu.query)
                .notEmptySet(menu.getIsFrame(),qSysMenu.isFrame)
                .notEmptySet(menu.getIsCache(),qSysMenu.isCache)
                .notEmptySet(menu.getMenuType(),qSysMenu.menuType)
                .notEmptySet(menu.getVisible(),qSysMenu.visible)
                .notEmptySet(menu.getStatus(),qSysMenu.status)
                .notEmptySet(menu.getPerms(),qSysMenu.perms)
                .notEmptySet(menu.getIcon(),qSysMenu.icon)
                .notEmptySet(menu.getRemark(),qSysMenu.remark)
                .notEmptySet(menu.getUpdateBy(),qSysMenu.updateBy).build(qSysMenu.menuId.eq(menu.getMenuId())).execute();
    }
}
