package com.blog_restructure;

import com.blog_restructure.dao.UserDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserTest extends BlogRestructureApplicationTests {
//    @Autowired
//    private UserDao userdao;

    @Test
    public void getUser(){
        System.out.println("success");
//        String s = userdao.getUserName();
//        System.out.println(s);
    }
}
