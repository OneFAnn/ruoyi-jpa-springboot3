package com.ruoyi.system.repository;

import com.ruoyi.common.core.BaseRepository;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.system.domain.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
@NoRepositoryBean
public interface SysConfigRepository extends BaseRepository<SysConfig,Long> {

    SysConfig selectConfig(SysConfig config);

    List<SysConfig> selectConfigList(SysConfig config, PageDomain pageDomain);

    SysConfig checkConfigKeyUnique(String configKey);

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    int updateConfig(SysConfig config);
}
