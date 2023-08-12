package com.ruoyi.system.service.impl;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Supplier;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.OrderByBuilder;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.SelectBuilder;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SelectBooleanBuilder;
import com.ruoyi.system.domain.QSysConfig;
import com.ruoyi.system.repository.SysConfigRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.mapper.SysConfigMapper;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 参数配置 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService
{


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysConfigRepository sysConfigRepository;



    @Autowired
    private BlazeJPAQueryFactory blazeJPAQueryFactory;

     final QSysConfig sysConfig = QSysConfig.sysConfig;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init()
    {
        loadingConfigCache();
    }

    /**
     * 查询参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public SysConfig selectConfigById(Long configId)
    {
        SysConfig config = blazeJPAQueryFactory.selectFrom(sysConfig).where(sysConfig.configId.eq(configId)).fetchOne();
        return config;
    }

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey)
    {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue))
        {
            return configValue;
        }
        SysConfig retConfig = blazeJPAQueryFactory.selectFrom(sysConfig).where(sysConfig.configKey.eq(configKey)).fetchOne();
        if (StringUtils.isNotNull(retConfig))
        {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     * 
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled()
    {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled))
        {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }


    @Autowired
    private CriteriaBuilderFactory criteriaBuilderFactory;

    /**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */

    @Override
    public List<SysConfig> selectConfigList(SysConfig config, PageDomain pageDomain)
    {
        BlazeJPAQuery<SysConfig> sysConfigJPAQuery = blazeJPAQueryFactory.selectFrom(sysConfig);
        sysConfigJPAQuery.where(SelectBooleanBuilder.builder()
                .notBlankLike(config.getConfigKey(), sysConfig.configKey)
                .notBlankLike(config.getConfigName(), sysConfig.configName)
                .notBlankEq(config.getConfigType(), sysConfig.configType)
                .notBlankDateAfter((String)config.getParams().get("beginTime"), sysConfig.createTime)
                .notBlankDateBefter((String)config.getParams().get("endTime"), sysConfig.createTime, localDate -> LocalTime.of(23, 59, 59))
                .build());
        Optional<List<SysConfig>> result = Optional.ofNullable(pageDomain).map(page->{
            Optional<OrderSpecifier> dslOrderBy = pageDomain.getDslOrderBy();
            dslOrderBy.map(orderSpecifier -> sysConfigJPAQuery.orderBy(orderSpecifier))
                    .orElse(sysConfigJPAQuery.orderBy(sysConfig.configId.desc()));
            return sysConfigJPAQuery.fetchPage(pageDomain.offset(), pageDomain.getPageSize());
        });
        return result.orElseGet(sysConfigJPAQuery::fetch);

    }

    /**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config)
    {
//        int row = configMapper.insertConfig(config);
        config = sysConfigRepository.save(config);
        if (config.getConfigId() != null)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return 1;
    }

    /**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config)
    {
//        SysConfig temp = configMapper.selectConfigById(config.getConfigId());
        SysConfig temp = sysConfigRepository.findById(config.getConfigId()).get();
        if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey()))
        {
            redisCache.deleteObject(getCacheKey(temp.getConfigKey()));
        }
//        int row = configMapper.updateConfig(config);
        sysConfigRepository.save(config);
        redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());

        return 1;
    }

    /**
     * 批量删除参数信息
     * 
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds)
    {
        for (Long configId : configIds)
        {
//            SysConfig config = selectConfigById(configId);
            sysConfigRepository.findById(configId).ifPresent(c->{
                if (StringUtils.equals(UserConstants.YES, c.getConfigType())){
                    throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", c.getConfigKey()));
                }
                sysConfigRepository.deleteById(configId);
                redisCache.deleteObject(getCacheKey(c.getConfigKey()));
            });

//            if (StringUtils.equals(UserConstants.YES, config.getConfigType()))
//            {
//                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
//            }
//            configMapper.deleteConfigById(configId);


        }
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache()
    {
//        List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
        List<SysConfig> configsList = sysConfigRepository.findAll();
        for (SysConfig config : configsList)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache()
    {
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache()
    {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config)
    {
        Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
//        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        SysConfig info = blazeJPAQueryFactory.selectFrom(sysConfig).where(sysConfig.configKey.eq(config.getConfigKey())).fetchOne();
        if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey)
    {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }
}
