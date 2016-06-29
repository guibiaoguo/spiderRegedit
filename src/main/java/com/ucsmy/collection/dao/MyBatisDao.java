package com.ucsmy.collection.dao;

/**
 * Created by Administrator on 2016/5/12.
 */

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

public class MyBatisDao<T> {
    private Class<T> type;

    @Resource(name = "secondaryDataSource")
    private SqlSessionTemplate sqlSessionTemplate;

    public void test() {
        sqlSessionTemplate.selectOne("selectList");
    }

}

