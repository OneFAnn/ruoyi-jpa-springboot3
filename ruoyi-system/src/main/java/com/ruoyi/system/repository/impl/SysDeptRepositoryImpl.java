package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.FilteredClause;
import com.querydsl.core.Tuple;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.domain.entity.QSysDept;
import com.ruoyi.common.core.domain.entity.QSysUser;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.common.utils.UpdateBooleanBuilder;
import com.ruoyi.system.domain.QSysRoleDept;
import com.ruoyi.system.repository.SysDeptRepository;
import com.ruoyi.system.repository.SysRoleRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class SysDeptRepositoryImpl extends BaseRepositoryImpl<SysDept,Long> implements SysDeptRepository {
    public SysDeptRepositoryImpl( EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysDept.class, em, factory);
    }
    final QSysDept qSysDept = QSysDept.sysDept;
    final QSysUser qSysUser = QSysUser.sysUser;
    final QSysRoleDept qSysRoleDept = QSysRoleDept.sysRoleDept;

    @Autowired
    private SysRoleRepository roleRepository;
    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        BlazeJPAQuery<SysDept> deptBlazeJPAQuery = blazeJPAQueryFactory.selectFrom(qSysDept)
                .where(SelectBooleanBuilder.builder()
                        .notEmptyEq(dept.getDeptId(), qSysDept.deptId)
                        .notEmptyEq(dept.getParentId(), qSysDept.parentId)
                        .notEmptyLike(dept.getDeptName(), qSysDept.deptName)
                        .notEmptyEq(dept.getStatus(), qSysDept.status)
                        .notEmptyEq("0",qSysDept.delFlag)
                        .notEmptyExpressions(dept.getParams())
                        .build()
                );
        return deptBlazeJPAQuery.fetch();


    }

    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        Optional<SysRole> optional = roleRepository.findById(roleId);
        BlazeJPAQuery<Long> jpaQuery = blazeJPAQueryFactory
                .select(qSysDept.deptId)
                .from(qSysDept).leftJoin(qSysRoleDept).on(qSysRoleDept.deptId.eq(qSysDept.deptId))
                .where(qSysRoleDept.roleId.eq(roleId))
                .orderBy(qSysDept.parentId.asc(),qSysDept.orderNum.asc());
        QSysRoleDept rd = new QSysRoleDept("rd");
        QSysDept d = new QSysDept("d");
        optional.ifPresent(r->{
            if (r.isDeptCheckStrictly()){
                jpaQuery.where(qSysDept.deptId.notIn(
                        JPAExpressions.select(d.parentId).from(d)
                                .innerJoin(rd)
                                .on(d.deptId.eq(rd.deptId).and(rd.roleId.eq(roleId)))
                ));
            }
        });
        return jpaQuery.fetch();
    }

    @Override
    public SysDept selectDeptById(Long deptId) {
        QSysDept subDept = new QSysDept("subDept");
        Tuple tuple = blazeJPAQueryFactory
                .select(qSysDept, JPAExpressions.select(subDept.deptName).from(subDept).where(subDept.deptId.eq(qSysDept.parentId)))
                .from(qSysDept)
                .where(qSysDept.deptId.eq(deptId))
                .fetchOne();
        SysDept info = tuple.get(0,subDept.getType());
        info.setParentName(tuple.get(1,String.class));
        return info;
    }

    @Override
    public List<SysDept> selectChildrenDeptById(Long deptId) {
        return  blazeJPAQueryFactory.selectFrom(qSysDept).where(Expressions.booleanTemplate("function('FIND_IN_SET',{0},{1})>0", deptId, qSysDept.ancestors)).fetch();

    }

    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return Math.toIntExact(blazeJPAQueryFactory.selectFrom(qSysDept)
                .where(qSysDept.status.eq("0")
                        .and(Expressions.booleanTemplate("function('FIND_IN_SET',{0},{1})>0", deptId, qSysDept.ancestors))).fetchCount());
    }

    @Override
    public long hasChildByDeptId(Long deptId) {
        return blazeJPAQueryFactory.selectFrom(qSysDept).where(qSysDept.delFlag.eq("0").and(qSysDept.parentId.eq(deptId))).fetchCount();
    }

    @Override
    public long checkDeptExistUser(Long deptId) {
        return blazeJPAQueryFactory.selectFrom(qSysUser).where(qSysUser.deptId.eq(deptId).and(qSysUser.delFlag.eq("0"))).fetchCount();

    }

    @Override
    public SysDept checkDeptNameUnique(String deptName, Long parentId) {
        return  blazeJPAQueryFactory.selectFrom(qSysDept).where(qSysDept.parentId.eq(parentId).and(qSysDept.deptName.eq(deptName)).and(qSysDept.delFlag.eq("0"))).fetchOne();
    }

    @Override
    public void updateDeptStatusNormal(Long[] deptIds) {
        blazeJPAQueryFactory.update(qSysDept).set(qSysDept.status, "0").where(qSysDept.deptId.in(deptIds)).execute();
    }

    @Override
    public int updateDept(SysDept dept) {
        UpdateClause<?> update = blazeJPAQueryFactory.update(qSysDept);
        return (int) UpdateBooleanBuilder.builder(update)
                .notEmptySet(dept.getParentId(), qSysDept.parentId)
                .notEmptySet(dept.getDeptName(), qSysDept.deptName)
                .notEmptySet(dept.getAncestors(), qSysDept.ancestors)
                .notEmptySet(dept.getOrderNum(), qSysDept.orderNum)
                .notEmptySet(dept.getLeader(), qSysDept.leader)
                .notEmptySet(dept.getPhone(), qSysDept.phone)
                .notEmptySet(dept.getEmail(), qSysDept.email)
                .notEmptySet(dept.getStatus(), qSysDept.status)
                .notEmptySet(dept.getUpdateBy(), qSysDept.updateBy).build(qSysDept.deptId.eq(dept.getDeptId())).execute();

    }

}
