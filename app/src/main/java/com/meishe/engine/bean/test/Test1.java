package com.meishe.engine.bean.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;

public class Test1 {
    public static void main(String[] strArr) throws IOException, CloneNotSupportedException {
        new ArrayList[10][1] = new ArrayList();
        System.out.println("1234");
        User user = new User();
        user.setId(1);
        user.setName("name1");
        user.setAge(18);
        user.setPpp(new Persion(true));
        System.out.println(new Gson().toJson(user));
        System.out.println(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(user));
        String json = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(user);
        System.out.println(json);
        System.out.println(((User) new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(json, User.class)).toString());
        System.out.println("=============1111=====================");
        System.out.println(((User) user.clone()).toString());
    }
}
