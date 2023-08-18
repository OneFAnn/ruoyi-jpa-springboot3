package com.ruoyi.quartz.repository;

import com.ruoyi.common.core.BaseRepository;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.quartz.domain.SysJobLog;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SysJobLogRepository extends BaseRepository<SysJobLog,Long> {
    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog, PageDomain pageDomain);




    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    public long deleteJobLogByIds(Long[] logIds);

}
