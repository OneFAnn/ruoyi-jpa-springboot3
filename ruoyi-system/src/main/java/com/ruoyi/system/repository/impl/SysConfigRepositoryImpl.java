package com.ruoyi.system.repository.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.FilteredClause;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.OrderSpecifier;
import com.ruoyi.common.core.BaseRepositoryImpl;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.common.utils.UpdateBooleanBuilder;
import com.ruoyi.system.domain.QSysConfig;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.repository.SysConfigRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public  class SysConfigRepositoryImpl extends BaseRepositoryImpl<SysConfig,Long> implements SysConfigRepository {

    final QSysConfig qSysConfig = QSysConfig.sysConfig;

    public SysConfigRepositoryImpl(EntityManager em, BlazeJPAQueryFactory factory) {
        super(SysConfig.class, em, factory);
    }

    @Override
    public SysConfig selectConfig(SysConfig config){
        return blazeJPAQueryFactory.selectFrom(qSysConfig)
                .where(SelectBooleanBuilder.builder()
                        .notEmptyEq(config.getConfigId(),qSysConfig.configId)
                        .notEmptyEq(config.getConfigKey(),qSysConfig.configKey)
                        .build()
                ).fetchOne();
    }

    @Override
    public List<SysConfig> selectConfigList(SysConfig config, PageDomain pageDomain) {
        BlazeJPAQuery<SysConfig> sysConfigJPAQuery = blazeJPAQueryFactory.selectFrom(qSysConfig);
        sysConfigJPAQuery.where(SelectBooleanBuilder.builder()
                .notEmptyLike(config.getConfigKey(), qSysConfig.configKey)
                .notEmptyLike(config.getConfigName(), qSysConfig.configName)
                .notEmptyEq(config.getConfigType(), qSysConfig.configType)
                .notEmptyDateAfter((String)config.getParams().get("beginTime"), qSysConfig.createTime)
                .notEmptyDateBefter((String)config.getParams().get("endTime"), qSysConfig.createTime, () -> LocalTime.of(23, 59, 59))
                .build()).orderBy(qSysConfig.configId.asc());
        return this.fetchPage(sysConfigJPAQuery,pageDomain).orElseGet(sysConfigJPAQuery::fetch);

    }

    @Override
    public SysConfig checkConfigKeyUnique(String configKey) {
        return blazeJPAQueryFactory.selectFrom(qSysConfig).where(qSysConfig.configKey.eq(configKey)).fetchOne();
    }

    @Override
    public int updateConfig(SysConfig config) {
        UpdateClause<?> update = blazeJPAQueryFactory.update(qSysConfig);
        return (int) UpdateBooleanBuilder.builder(update)
                .notEmptySet(config.getConfigName(), qSysConfig.configName)
                .notEmptySet(config.getConfigKey(),qSysConfig.configKey)
                .notEmptySet(config.getConfigValue(),qSysConfig.configValue)
                .notEmptySet(config.getConfigType(),qSysConfig.configType)
                .notEmptySet(config.getUpdateBy(),qSysConfig.updateBy)
                .notEmptySet(config.getRemark(),qSysConfig.remark)
                .notEmptySet(new Date(),qSysConfig.updateTime)
                .build(qSysConfig.configId.eq(config.getConfigId())).execute();
    }
}
