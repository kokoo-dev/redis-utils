package com.kokoo.redisutils.config

import com.kokoo.redisutils.dto.PubSubDto
import com.kokoo.redisutils.event.Subscriber
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    private val redisProperties: RedisProperties,
    private val pubSubProperties: BaseProperties.PubSub
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(redisProperties.host, redisProperties.port)
    }

    @Bean
    fun pubSubRedisTemplate(): RedisTemplate<String, PubSubDto> {
        val redisTemplate = RedisTemplate<String, PubSubDto>()
        redisTemplate.connectionFactory = redisConnectionFactory()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(PubSubDto::class.java)

        return redisTemplate
    }

    @Bean
    fun redisListenerContainer(
        redisConnectionFactory: RedisConnectionFactory,
        subscriber: Subscriber
    ): RedisMessageListenerContainer {
        val redisMessageListenerContainer = RedisMessageListenerContainer()
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory)

        redisMessageListenerContainer.addMessageListener(
            subscriber,
            ChannelTopic(pubSubProperties.topicName)
        )

        return redisMessageListenerContainer
    }
}