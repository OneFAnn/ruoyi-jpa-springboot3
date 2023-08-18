package com.ruoyi.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;
import org.hibernate.annotations.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 部门表 sys_dept
 * 
 * @author ruoyi
 */
@Entity
@DynamicUpdate
@DynamicInsert
public class SysDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("部门id")
    private Long deptId;

    /** 父部门ID */
    @ColumnDefault("0")
    @Comment("父部门id")
    private Long parentId;

    /** 祖级列表 */
    @Comment("祖级列表")
    @Column(length = 50)
    @ColumnDefault("''")
    private String ancestors;

    /** 部门名称 */
    @Comment("部门名称")
    @ColumnDefault("''")
    @Column(length = 50)
    private String deptName;

    /** 显示顺序 */
    @Comment("显示顺序")
    @ColumnDefault("0")
    @Column(length = 4)
    private Integer orderNum;

    /** 负责人 */
    @Comment("负责人")
    @Column(length = 20)
    private String leader;

    /** 联系电话 */
    @Comment("联系电话")
    @Column(length = 11)
    private String phone;

    /** 邮箱 */
    @Comment("邮箱")
    @Column(length = 50)
    private String email;

    /** 部门状态:0正常,1停用 */
    @Comment("部门状态:0正常,1停用")
    @ColumnDefault("'0'")
    @Column(length = 1)
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    @Comment("删除标志（0代表存在 2代表删除）")
    @ColumnDefault("'0'")
    @Column(length = 1)
    private String delFlag;

    /** 父部门名称 */
    @Comment("父部门名称")
    @Transient
    private String parentName;
    
    /** 子部门 */
    @Comment("子部门")
    @Transient
    private List<SysDept> children = new ArrayList<SysDept>();

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getLeader()
    {
        return leader;
    }

    public void setLeader(String leader)
    {
        this.leader = leader;
    }

    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public List<SysDept> getChildren()
    {
        return children;
    }

    public void setChildren(List<SysDept> children)
    {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deptId", getDeptId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("deptName", getDeptName())
            .append("orderNum", getOrderNum())
            .append("leader", getLeader())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }


}
