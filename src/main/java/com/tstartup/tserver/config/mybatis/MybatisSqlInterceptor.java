package com.tstartup.tserver.config.mybatis;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;


@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Component
public class MybatisSqlInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger("Interceptor");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {//执行sql过程
            return invocation.proceed();
        } finally {
            try {
                long endTime = System.currentTimeMillis();
                //获取xml中的一个select/update/insert/delete节点，主要描述的是一条SQL语句
                MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
                Object parameter = "";
                if (invocation.getArgs().length > 1) {
                    parameter = invocation.getArgs()[1];
                    if (Objects.nonNull(parameter)) {
                        //打印入参参数
                        logger.info("==> transmit Parameters is [ {} ]", parameter);
                    }
                }
                // 获取到节点的id,即sql语句的id
                String sqlId = mappedStatement.getId();
                String[] mappers = getMapper(sqlId);
                //打印执行的Mapper名称
                logger.info("==> execute Mapper name is [ {} ]", mappers[0]);
                //打印执行的Mapper方法
                logger.info("==> execute Mapper method is [ {} ]", mappers[1]);
                // BoundSql就是封装myBatis最终产生的sql类
                BoundSql boundSql = mappedStatement.getBoundSql(parameter);
                // 获取节点的配置
                Configuration configuration = mappedStatement.getConfiguration();
                String sql = getSql(configuration, boundSql);
                //打印执行带参SQL
                logger.info("==> execute SQL with Parameters is [ {} ]", sql);
                //打印SQL执行时间
                logger.info("==> execute SQL cost [ {} ] ms", (endTime - startTime));
                long l = endTime - startTime;

            } catch (Exception e) {
                logger.error("-----mybatis-----", e);
            }
        }
    }

    /**
     * 根据节点id获取执行的Mapper name和Mapper method
     *
     * @param sqlId 节点id
     * @return
     */
    public static String[] getMapper(String sqlId) {
        int index = sqlId.lastIndexOf(".");
        String mapper = sqlId.substring(0, index);
        String method = sqlId.substring(index + 1);
        return new String[]{mapper, method};
    }

    /**
     * 获取带参SQL
     *
     * @param configuration 节点的配置
     * @param boundSql      封装myBatis最终产生的sql类
     * @return
     */
    public static String getSql(Configuration configuration, BoundSql boundSql) {
        //获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql
                .getParameterMappings();
        //sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            //获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换。如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                //MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        //该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        //打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }

    /**
     * 对SQL进行格式化
     *
     * @param obj
     * @return
     */
    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}