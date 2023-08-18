import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruoyi.RuoYiApplication;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.system.domain.QSysConfig;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.repository.SysConfigRepository;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.impl.SysConfigServiceImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = RuoYiApplication.class)

public class JpaTest {
    private static final Logger log = LoggerFactory.getLogger(JpaTest.class);
    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Test
    void test1(){
        List<SysConfig> all = sysConfigRepository.findAll();
        System.out.println(all);
    }

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

//    @PersistenceContext
//    private EntityManager entityManager;
    @Test
    void  t(){
        QSysConfig qSysConfig = QSysConfig.sysConfig;
        JPAQuery<SysConfig> sysConfigJPAQuery = jpaQueryFactory.selectFrom(qSysConfig);
        sysConfigJPAQuery.where(qSysConfig.configKey.eq("1"));
        sysConfigJPAQuery.where(qSysConfig.configName.eq("1212121"));
        List<SysConfig> fetch = sysConfigJPAQuery.fetch();
        log.info("{}",fetch);
//        log.info("{}",sysConfigRepository.count());
    }

    @Test
    void t2(){
        SysConfig config = new SysConfig();
        config.setConfigKey("xcx");
        config.setConfigName("xxxxx");
        config.setConfigValue("dsfadfs");
        config = sysConfigRepository.save(config);
        config.setConfigId(null);
        config.setConfigName("2222222222");
        sysConfigRepository.save(config);
        System.out.println(config);
    }

    @Autowired
    private ISysConfigService sysConfigService;

    @Test
    void t3(){
        SysConfig config = new SysConfig();
        config.setConfigName("x");
        config.setConfigKey("11");
        List<SysConfig> sysConfigs = sysConfigService.selectConfigList(config,null);
        System.out.println(sysConfigs);
    }

    @Autowired
    private BlazeJPAQueryFactory blazeJPAQueryFactory;

    @Test
    void t4(){
        QSysConfig  sysConfig = QSysConfig.sysConfig;
        List fetch = blazeJPAQueryFactory.selectFrom(sysConfig).fetch();
        SysConfig config = blazeJPAQueryFactory.selectFrom(sysConfig).fetchFirst();
    }

    @Autowired
    private ISysDeptService sysDeptService;
    @Test
    void t5(){

//        List<Long> longs = sysDeptService.selectDeptListByRoleId(2l);
//        System.out.println(longs);
        int i = sysDeptService.selectNormalChildrenDeptById(100l);
        System.out.println(i);
    }



}
