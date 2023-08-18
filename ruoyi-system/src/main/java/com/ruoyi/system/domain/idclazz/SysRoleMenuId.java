package com.ruoyi.system.domain.idclazz;

import jakarta.persistence.Id;

public class SysRoleMenuId {
    /** 角色ID */
    @Id
    private Long roleId;

    /** 菜单ID */
    @Id
    private Long menuId;

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getMenuId()
    {
        return menuId;
    }

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

}
