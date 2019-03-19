package due.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author due
 */
@Configuration
@EnableCaching
public class RedisConfig {
    /*@Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }*/
    //声明了CacheManager后，所有的cachename 必须 包含在cachemanager.cacheNames中，不然： Cannot find cache named 'cachename' for Builder
    /*@Bean("springCM")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("entities");
    }*/
    //设置默认序列化方式
    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> template) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(template.getConnectionFactory());
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(template.getValueSerializer()));
        return new RedisCacheManager(redisCacheWriter, configuration);
    }
    //设置默认序列化方式
    /*@Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jsonRedisSerializer.setObjectMapper(om);
        return RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer));
    }*/

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTpl = new RedisTemplate<>();
        redisTpl.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        redisTpl.setKeySerializer(stringSerializer);
        // hash的key也采用String的序列化方式
        redisTpl.setHashKeySerializer(stringSerializer);
        // value序列化方式采用jackson
        redisTpl.setValueSerializer(jsonRedisSerializer);
        //hash的value序列化方式采用jackson
        redisTpl.setHashValueSerializer(jsonRedisSerializer);
        redisTpl.afterPropertiesSet();
        return redisTpl;
    }
}
