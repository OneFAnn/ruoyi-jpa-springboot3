package com.ruoyi.system.repository;

import com.ruoyi.common.core.BaseRepository;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.idclazz.SysUserRoleId;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface SysUserRoleRepository extends BaseRepository<SysUserRole, SysUserRoleId> {
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    public long deleteUserRoleByUserId(Long userId);

    /**
     * 批量删除用户和角色关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public long deleteUserRole(Long[] ids);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public long countUserRoleByRoleId(Long roleId);


    /**
     * 删除用户和角色关联信息
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    public long deleteUserRoleInfo(SysUserRole userRole);

    /**
     * 批量取消授权用户角色
     *
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public long deleteUserRoleInfos(Long roleId, Long[] userIds);

}
