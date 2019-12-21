package com.blog_restructure.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Date;
//
//@SpringBootApplication
//@MapperScan(basePackages = "com.blog_restructure.dao")
public class UserDao {
    public String getUserName() {
        return "this is UserDao";
    }
}
