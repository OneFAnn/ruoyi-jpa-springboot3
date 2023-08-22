package com.ruoyi.generator.repository;

import com.ruoyi.common.core.BaseRepository;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.generator.domain.GenTable;
import com.ruoyi.generator.domain.idclass.TablesViewId;
import com.ruoyi.generator.domain.views.Tables;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface TablesViewRepository extends BaseRepository<Tables, TablesViewId> {

    List<GenTable> selectDbTableList(GenTable genTable, PageDomain pageDomain);

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    public List<Tables> selectDbTableListByNames(String[] tableNames);

}
