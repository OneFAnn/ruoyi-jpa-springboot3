package com.ruoyi.common.utils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public  class SelectBooleanBuilder{
    public static SelectBooleanBuilder builder(){
        return new SelectBooleanBuilder();
    }

    private final BooleanBuilder booleanBuilder;

    protected SelectBooleanBuilder() {
        this.booleanBuilder = new BooleanBuilder();
    }

    public SelectBooleanBuilder notBlankEq(String param, StringPath stringPath){
            if(StringUtils.hasText(param)){
                booleanBuilder.and(stringPath.eq(param));
            }
            return this;
    }
    public SelectBooleanBuilder notBlankLike(String param, StringPath stringPath){
        if(StringUtils.hasText(param)){
            booleanBuilder.and(stringPath.like("%"+param+"%"));
        }
        return this;
    }

    public SelectBooleanBuilder custom(Supplier<Predicate> supplier){
        booleanBuilder.and(supplier.get());
        return this;
    }

    public SelectBooleanBuilder notBlankDateAfter(String param, DateTimePath<Date> path){
        if(StringUtils.hasText(param)){
            Date begin = DateUtils.parseDate(param);
            booleanBuilder.and(path.after(begin));
        }
        return this;
    }

    public SelectBooleanBuilder notBlankDateBefter(String param, DateTimePath<Date> path,Function<LocalDate, LocalTime> function){
        if(StringUtils.hasText(param)){
            LocalDate end = LocalDate.parse(param);
            LocalDateTime localDateTime= end.atTime(function.apply(end));
            booleanBuilder.and(path.before(DateUtils.toDate(localDateTime)));
        }
        return this;
    }

    public BooleanBuilder build(){
        return booleanBuilder;
    }
}

