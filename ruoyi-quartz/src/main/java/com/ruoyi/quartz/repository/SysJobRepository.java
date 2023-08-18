package com.ruoyi.quartz.repository;

import com.ruoyi.common.core.BaseRepository;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.quartz.domain.SysJob;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SysJobRepository extends BaseRepository<SysJob,Long> {

    /**
     * 查询调度任务日志集合
     *
     * @param job 调度信息
     * @return 操作日志集合
     */
    public List<SysJob> selectJobList(SysJob job, PageDomain pageDomain);


    /**
     * 批量删除调度任务信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public long deleteJobByIds(Long[] ids);



}
