package com.eplugger.gson;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
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
public class ExposeTest {
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
     * 如果您使用Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()创建Gson，
     * 则Gson的toJson()和fromJson()方法将排除密码字段。这是因为密码字段没有使用@Expose注解进行标记。
     * 由于serialize设置为 false，Gson还将从序列化中排除lastName和emailAddress。
     * 同样，Gson将从反序列化中排除emailAddress，因为反序列化设置为false。
     * 请注意，实现相同效果的另一种方法是将密码字段标记为瞬态，即使使用默认设置，Gson也会将其排除。
     * \@Expose注解在一种编程风格中很有用，在这种风格中，您希望明确指定所有应考虑进行序列化或反序列化的字段。
     */
    @Test
    public void testBuild() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(user);
        log.debug(json);
        json = "{\"firstName\":\"张\",\"lastName\":\"三\",\"emailAddress\":\"北京市\",\"password\":\"1234\"}";
        log.debug(gson.fromJson(json, User.class).toString());
    }

}

@AllArgsConstructor
@NoArgsConstructor
@Data
class User {
    @Expose
    private String firstName;
    @Expose(serialize = false)
    private String lastName;
    @Expose(serialize = false, deserialize = false)
    private String emailAddress;
    private String password;
}