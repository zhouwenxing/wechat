package com.wechat.redis;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {
	
	   
    @SuppressWarnings("rawtypes")
	@Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
	   RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
	   return cacheManager;
    }
    
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
	   RedisTemplate<Object, Object> template = new RedisTemplate<Object, Object>();
	   template.setConnectionFactory(factory);
	   //对于session，手动转成byte[]存入redis，取出时会判断valueSerializer()是否为空，不为空会再进行反序列
	   //具体原因尚未发现，可能两次反序列最后导致直接返回字符串。如果手动 template.setValueSerializer(null)，
	   //会导致手动将byte[]转session报错invalid stream header，故暂时先屏蔽，后面再仔细研究
//	   setSerializer(template); //设置序列化工具，这样ReportBean不需要实现Serializable接口
	   template.afterPropertiesSet();
	   return template;
    }
//    private void setSerializer(RedisTemplate<Object, Object> template) {
//       Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//	   ObjectMapper om = new ObjectMapper();
//	   om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//	   om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//	   jackson2JsonRedisSerializer.setObjectMapper(om);
//       StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//	   template.setValueSerializer(stringRedisSerializer);
//	   template.setKeySerializer(new StringRedisSerializer());
//    }

}
