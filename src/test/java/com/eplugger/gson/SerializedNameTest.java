package com.eplugger.gson;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Slf4j
public class SerializedNameTest {
    @Test
    public void testName() {
        MyClass target = new MyClass("v1", "v2", "v3");
        Gson gson = new Gson();
        String json = gson.toJson(target);
        log.debug(json);
    }

    @Test
    public void testName1() {
        Gson gson = new Gson();
        MyClass target = gson.fromJson("{'name1':'v1'}", MyClass.class);
        assertEquals("v1", target.b);
        target = gson.fromJson("{'name2':'v2'}", MyClass.class);
        assertEquals("v2", target.b);
        target = gson.fromJson("{'name3':'v3'}", MyClass.class);
        assertEquals("v3", target.b);
    }
}

class MyClass {
    @SerializedName("name")
    String a;
    @SerializedName(value = "name1", alternate = {"name2", "name3"})
    String b;
    String c;

    public MyClass(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}