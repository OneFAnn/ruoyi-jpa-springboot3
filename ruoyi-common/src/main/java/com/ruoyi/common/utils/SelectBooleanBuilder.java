package com.ruoyi.common.utils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

public  class SelectBooleanBuilder{
    public static SelectBooleanBuilder builder(){
        return new SelectBooleanBuilder();
    }

    private final BooleanBuilder booleanBuilder;

    protected SelectBooleanBuilder() {
        this.booleanBuilder = new BooleanBuilder();
    }

    public SelectBooleanBuilder notEmptyEq(String param, StringPath stringPath){
            if(StringUtils.hasText(param)){
                booleanBuilder.and(stringPath.eq(param));
            }
            return this;
    }


    public SelectBooleanBuilder notEmptyEqOrIn(Long param, NumberPath numberPath, JPQLQuery<Long> ins ){
        if(param!=null){
            booleanBuilder.and(numberPath.eq(param).or(numberPath.in(ins)));
        }
        return this;
    }

    public SelectBooleanBuilder notEmptyLike(String param, StringPath stringPath){
        if(StringUtils.hasText(param)){
            booleanBuilder.and(stringPath.like("%"+param+"%"));
        }
        return this;
    }

    public SelectBooleanBuilder notEmptyNotLike(String param, StringPath stringPath){
        if(StringUtils.hasText(param)){
            booleanBuilder.and(stringPath.notLike("%"+param+"%"));
        }
        return this;
    }


    public SelectBooleanBuilder notEmptyIn(Integer[] param, NumberPath numberPath){
        if(param!=null && param.length>0){
            booleanBuilder.and(numberPath.in(param));
        }
        return this;
    }

    public SelectBooleanBuilder notEmptyIn(String[] params, StringPath path){
        if(params!=null && params.length>0){
            booleanBuilder.and(path.in(params));
        }
        return this;
    }


    public SelectBooleanBuilder notEmptyExpressions(BooleanExpression booleanExpression){
        if(booleanExpression!=null){
            booleanBuilder.and(booleanExpression);
        }
        return this;
    }
    //添加数据权限条件
    public SelectBooleanBuilder notEmptyExpressions(Map<String,Object> params){
        if(params.get("dataScope")!=null && (params.get("dataScope")) instanceof BooleanExpression ){
            booleanBuilder.and((BooleanExpression) params.get("dataScope"));
        }
        return this;
    }
    public SelectBooleanBuilder notEmptyEq(Integer number, NumberPath numberPath){
        if (number!=null){
            booleanBuilder.and(numberPath.eq(number));
        }
        return this;
    }

    public SelectBooleanBuilder notEmptyEq(Long number, NumberPath numberPath){
            if (number!=null){
                booleanBuilder.and(numberPath.eq(number));
            }
            return this;
    }

    public SelectBooleanBuilder custom(Supplier<Predicate> supplier){
        booleanBuilder.and(supplier.get());
        return this;
    }

    public SelectBooleanBuilder notEmptyDateAfter(String param, DateTimePath<Date> path){
        if(StringUtils.hasText(param)){
            Date begin = DateUtils.parseDate(param);
            booleanBuilder.and(path.after(begin));
        }
        return this;
    }



    public SelectBooleanBuilder notEmptyDateBefter(String param, DateTimePath<Date> path, Supplier<LocalTime> supplier){
        if(StringUtils.hasText(param)){
            LocalDate end = LocalDate.parse(param);
            LocalDateTime localDateTime= end.atTime(supplier.get());
            booleanBuilder.and(path.before(DateUtils.toDate(localDateTime)));
        }
        return this;
    }

    public BooleanBuilder build(){
        return booleanBuilder;
    }
}

