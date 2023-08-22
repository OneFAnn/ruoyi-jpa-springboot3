package com.ruoyi.generator.domain.idclass;

import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class TablesViewId implements Serializable {

    private String tableName;

    private String tableSchema;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TablesViewId that = (TablesViewId) o;
        return Objects.equals(tableName, that.tableName) && Objects.equals(tableSchema, that.tableSchema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, tableSchema);
    }
}
