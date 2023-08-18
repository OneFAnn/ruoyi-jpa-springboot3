package com.ruoyi.system.domain.idclazz;

import jakarta.persistence.Entity;

import java.io.Serializable;

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
}
