package com.asiczen.analytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.asiczen.analytics.model.EndOfDayMessage;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {

	@Bean
	LettuceConnectionFactory jedisConnectionFactory() {
		return new LettuceConnectionFactory();
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		return mapper;
	}
	
	@Bean(name = "convertedmsgtemplate")
	public RedisTemplate<String, EndOfDayMessage> redisConvertedMessgeTemplate() {
		RedisTemplate<String, EndOfDayMessage> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());

		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));

		return template;
	}
}
