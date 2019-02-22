package due.demo;

import due.demo.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTests {
    @Autowired
    private StringRedisTemplate sRedis;

    @Autowired
    private RedisTemplate<String, Object> redis;

    @Test
    public void test() {
        sRedis.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", sRedis.opsForValue().get("aaa"));
    }

    @Test
    public void test2() throws Exception {
        User user = new User("aaa", 22);
        ValueOperations<String, Object> op = redis.opsForValue();
        op.set("due.user", user);
        op.set("due.user.f", user, 1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        Assert.assertEquals(false, redis.hasKey("due.user.f"));
    }

}
