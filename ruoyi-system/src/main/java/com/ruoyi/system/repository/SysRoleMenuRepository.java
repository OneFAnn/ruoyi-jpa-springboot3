package com.ruoyi.system.repository;

import com.ruoyi.common.core.BaseRepository;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.domain.idclazz.SysRoleMenuId;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface SysRoleMenuRepository extends BaseRepository<SysRoleMenu, SysRoleMenuId> {
    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public long checkMenuExistRole(Long menuId);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public long deleteRoleMenuByRoleId(Long roleId);

    /**
     * 批量删除角色菜单关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public long deleteRoleMenu(Long[] ids);


}
