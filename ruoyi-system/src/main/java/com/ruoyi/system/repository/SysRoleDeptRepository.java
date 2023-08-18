package com.ruoyi.system.repository;

import com.ruoyi.common.core.BaseRepository;
import com.ruoyi.system.domain.SysRoleDept;
import com.ruoyi.system.domain.idclazz.SysRoleDeptId;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SysRoleDeptRepository extends BaseRepository<SysRoleDept, SysRoleDeptId> {
    /**
     * 通过角色ID删除角色和部门关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public long deleteRoleDeptByRoleId(Long roleId);

    /**
     * 批量删除角色部门关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public long deleteRoleDept(Long[] ids);

    /**
     * 查询部门使用数量
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public long selectCountRoleDeptByDeptId(Long deptId);


}
