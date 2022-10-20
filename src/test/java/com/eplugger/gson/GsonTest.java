package com.eplugger.gson;

import java.sql.Date;

import cn.hutool.core.date.DateUtil;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class GsonTest {
    @Test
    public void testJson() {
        Gson gson = new Gson();
        User1 user = new User1();
        user.idNum = "123";
        user.name = "张三";
        user.age = 12;
        user.date = DateUtil.date().toSqlDate();
        String json = gson.toJson(user);
        log.debug(json);
        User2 user2 = gson.fromJson(json, User2.class);
        log.debug(user2.toString());
    }
}

class User1 {
    @SerializedName("idNo")
    String idNum;
    @SerializedName("name1")
    String name;
    @SerializedName("age1")
    int age;
    Date date;
}

@ToString
class User2 {
    String idNo;
    String name1;
    int age1;
    Date date;
}