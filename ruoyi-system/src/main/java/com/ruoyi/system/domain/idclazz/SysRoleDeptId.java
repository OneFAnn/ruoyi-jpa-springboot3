package com.ruoyi.system.domain.idclazz;

import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.Objects;

public class SysRoleDeptId implements  Serializable{
    private Long roleId;
    private Long deptId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysRoleDeptId that = (SysRoleDeptId) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(deptId, that.deptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, deptId);
    }
}
