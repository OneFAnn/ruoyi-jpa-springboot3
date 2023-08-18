package com.ruoyi.system.repository;

import com.ruoyi.common.core.BaseRepository;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.system.domain.SysLogininfor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface SysLogininforRepository extends BaseRepository<SysLogininfor,Long> {



    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor, PageDomain pageDomain);





}
