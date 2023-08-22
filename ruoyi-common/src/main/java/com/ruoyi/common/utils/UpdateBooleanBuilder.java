package com.ruoyi.common.utils;

import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import org.springframework.util.StringUtils;

import java.util.Date;

public class UpdateBooleanBuilder {
    public static UpdateBooleanBuilder builder(UpdateClause update){
        return new UpdateBooleanBuilder(update);
    }
    private UpdateClause<?> updateClause;
    public UpdateBooleanBuilder(UpdateClause updateClause) {
        this.updateClause = updateClause;
    }

    public UpdateClause<?> build(Predicate where){
        return this.updateClause.where(where);
    }

    public UpdateBooleanBuilder notEmptySet(String param,StringPath stringPath){
        if (StringUtils.hasText(param)){
            this.updateClause.set(stringPath,param);
        }
        return this;
    }

    public UpdateBooleanBuilder notEmptySet(Character param,ComparablePath comparablePath){
        if (param!=null){
            this.updateClause.set(comparablePath,param.toString());
        }
        return this;
    }

    public UpdateBooleanBuilder notEmptySet(Long param, NumberPath numberPath){
        if (param!=null){
            this.updateClause.set(numberPath,param);
        }
        return this;
    }

    public UpdateBooleanBuilder notEmptySet(Integer param, NumberPath numberPath){
        if (param!=null){
            this.updateClause.set(numberPath,param);
        }
        return this;
    }

    public UpdateBooleanBuilder notEmptySet(Date param, DateTimePath datePath){
        if (param!=null){
            this.updateClause.set(datePath,param);
        }
        return this;
    }

    public UpdateBooleanBuilder notEmptySet(Boolean param, BooleanPath booleanPath){
        if (param!=null){
            this.updateClause.set(booleanPath,param);
        }
        return this;
    }
}
