package com.ruoyi.system.domain;

import com.ruoyi.system.domain.idclazz.SysRoleDeptId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 角色和部门关联 sys_role_dept
 * 
 * @author ruoyi
 */
@Entity
@IdClass(SysRoleDeptId.class)
public class SysRoleDept
{
    /** 角色ID */
    @Id
    private Long roleId;
    
    /** 部门ID */
    @Id
    private Long deptId;

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("deptId", getDeptId())
            .toString();
    }
}

