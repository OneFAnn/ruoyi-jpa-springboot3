package com.ruoyi.generator.domain.views;

import com.ruoyi.generator.domain.idclass.TablesViewId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import java.util.Date;
@Entity
@Immutable
@IdClass(TablesViewId.class)
@Subselect("select table_comment,table_name,create_time,update_time,table_schema from information_schema.tables order by create_time")
public class Tables {
    @Id
    private String tableName;

    private String tableComment;
    private Date createTime;

    private Date updateTime;
    @Id
    private String tableSchema;

    public String getTableName() {
        return tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getTableSchema() {
        return tableSchema;
    }
}
