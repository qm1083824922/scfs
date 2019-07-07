package com.scfs.rpc.cache;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by Administrator on 2016/10/21.
 */
public class ObjectRedisTemplate<T> extends RedisTemplate<String, T> {
	public ObjectRedisTemplate() {
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<?> objectSerializer = new JdkSerializationRedisSerializer();
		setKeySerializer(stringSerializer);
		setValueSerializer(objectSerializer);
		setHashKeySerializer(stringSerializer);
		setHashValueSerializer(objectSerializer);
	}

	public ObjectRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}
}
