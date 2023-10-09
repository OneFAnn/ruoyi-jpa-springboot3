import com.ruoyi.RuoYiApplication;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.generator.domain.GenTable;
import com.ruoyi.generator.domain.GenTableColumn;
import com.ruoyi.generator.domain.views.Tables;
import com.ruoyi.generator.repository.TablesViewRepository;
import com.ruoyi.generator.repository.GenTableColumnRepository;
import com.ruoyi.generator.repository.GenTableRepository;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.repository.SysConfigRepository;
import com.ruoyi.system.repository.SysDeptRepository;
import com.ruoyi.system.repository.SysUserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        sysDept.setParentId(102l);
        sysDept.setOrderNum(1);
        sysDept.setDeptName("822");
        deptRepository.saveAndFlush(sysDept);
//        int i = 1/0;
    }
    @Autowired
    private SysUserRoleRepository userRoleRepository;
    @Test
    void t2(){
        System.out.println(Optional.empty().orElse(null));
    }


}
