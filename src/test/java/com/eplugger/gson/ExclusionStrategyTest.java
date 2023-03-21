package com.eplugger.gson;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * 此注解应用于JSON序列化或反序列化。
 * 除非您使用GsonBuilder构建Gson并调用GsonBuilder.excludeFieldsWithoutExposeAnnotation()方法，否则此注释无效。
 */
@Slf4j
public class ExclusionStrategyTest {
    private User user;
    @Before
    public void newUser() {
        user = new User("张", "三", "北京市", "1234");
    }
    /**
     * 如果您使用new Gson()创建了Gson，则toJson()和fromJson()方法将使用密码字段以及firstName、lastName和emailAddress进行序列化和反序列化。
     */
    @Test
    public void testNew() {
        Gson gson = new Gson();
        log.debug(gson.toJson(user));
    }

    /**
     * ExclusionStrategy的过滤字段，序列化
     */
    @Test
    public void testBuildSerialization() {
        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if ("password".equals(f.getName())) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
        String json = gson.toJson(user);
        log.debug(json);
    }

    /**
     * ExclusionStrategy的过滤字段，反序列化
     */
    @Test
    public void testBuildDeserialization() {
        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                if ("password".equals(f.getName())) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
        String json = "{\"firstName\":\"张\",\"lastName\":\"三\",\"emailAddress\":\"北京市\",\"password\":\"1234\"}";
        log.debug(gson.fromJson(json, User.class).toString());
    }

}

@AllArgsConstructor
@NoArgsConstructor
@Data
class User3 {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
}