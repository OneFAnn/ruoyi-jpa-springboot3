package com.ruoyi.system.service.impl;

import java.util.Arrays;
import java.util.List;

import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.system.repository.SysLogininforRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.SysLogininfor;

import com.ruoyi.system.service.ISysLogininforService;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysLogininforServiceImpl implements ISysLogininforService
{



    @Autowired
    private SysLogininforRepository logininforRepository;
    /**
     * 新增系统登录日志
     * 
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(SysLogininfor logininfor)
    {
        logininforRepository.save(logininfor);
    }

    /**
     * 查询系统登录日志集合
     * 
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor, PageDomain pageDomain)
    {
        return logininforRepository.selectLogininforList(logininfor,pageDomain);
    }

    /**
     * 批量删除系统登录日志
     * 
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds)
    {
        logininforRepository.deleteAllById(Arrays.asList(infoIds));
        return 1;
    }

    @Override
    public void cleanLogininfor() {
        logininforRepository.deleteAllInBatch();
    }


}
