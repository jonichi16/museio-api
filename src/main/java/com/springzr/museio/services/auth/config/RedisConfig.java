package com.springzr.museio.services.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;


/**
 * Configuration for Redis.
 *
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    String redisHostName;
    @Value("${spring.redis.port}")
    String redisPort;
    @Value("${spring.redis.password}")
    String redisPassword;

    /**
     * Bean for {@link LettuceConnectionFactory}.
     *
     * @return {@link LettuceConnectionFactory}
     */
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHostName);
        configuration.setPort(Integer.parseInt(redisPort));
        configuration.setPassword(redisPassword);
        return new LettuceConnectionFactory(configuration);
    }

    /**
     * Bean for {@link StringRedisTemplate}.
     *
     * @return {@link StringRedisTemplate}
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
