import com.ruoyi.RuoYiApplication;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.generator.repository.GenTableRepository;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.repository.SysConfigRepository;
import com.ruoyi.system.repository.SysDeptRepository;
import com.ruoyi.system.repository.SysUserRepository;
import com.ruoyi.system.repository.SysUserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootTest(classes = RuoYiApplication.class)
public class JpaTest2 {
    @Autowired
    private SysConfigRepository sysConfigRepository;
    @Autowired
    private SysDeptRepository deptRepository;

    @Test
    void  t(){
//        sysConfigRepository.findAll().forEach(sysConfig -> System.out.println(sysConfig));
//        sysConfigRepository.findByConfigName("111");
        SysDept sysDept = new SysDept();
//        sysDept.setDeptId(200l);
        sysDept.setOrderNum(1);
        sysDept.setDeptName("886");
        deptRepository.save(sysDept);
    }
    @Autowired
    private SysUserRoleRepository userRoleRepository;
    @Test
    void t2(){
        SysUserRole ur = new SysUserRole();
        ur.setRoleId(100l);
        ur.setUserId(200l);
        userRoleRepository.save(ur);
    }

    @Autowired
    private GenTableRepository genTableRepository;
    @Test
    void t3(){
        genTableRepository.selectGenTableAll();
    }



}
