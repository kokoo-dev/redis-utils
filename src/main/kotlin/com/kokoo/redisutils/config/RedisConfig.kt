package com.kokoo.redisutils.config

import com.kokoo.redisutils.pubsub.dto.PubSubDto
import com.kokoo.redisutils.pubsub.event.Subscriber
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.integration.redis.util.RedisLockRegistry
import org.springframework.integration.support.locks.ExpirableLockRegistry
import java.time.Duration

@Configuration
class RedisConfig(
    private val redisProperties: RedisProperties,
    private val pubSubProperties: BaseProperties.PubSub,
    private val lockRegistryProperties: BaseProperties.LockRegistry
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
    fun intRedisTemplate(): RedisTemplate<String, Int> {
        val redisTemplate = RedisTemplate<String, Int>()
        redisTemplate.connectionFactory = redisConnectionFactory()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = GenericJackson2JsonRedisSerializer()

        return redisTemplate
    }

    @Bean
    fun transactionRedisTemplate(): RedisTemplate<String, Int> {
        val redisTemplate = RedisTemplate<String, Int>()
        redisTemplate.connectionFactory = redisConnectionFactory()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = GenericJackson2JsonRedisSerializer()
        redisTemplate.setEnableTransactionSupport(true)

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

    @Bean
    fun lockRegistry(redisConnectionFactory: RedisConnectionFactory): ExpirableLockRegistry {
        val redisLockRegistry = RedisLockRegistry(
            redisConnectionFactory,
            lockRegistryProperties.key,
            Duration.ofSeconds(lockRegistryProperties.lockExpireSeconds).toMillis()
        )
        redisLockRegistry.setRedisLockType(RedisLockRegistry.RedisLockType.PUB_SUB_LOCK)

        return redisLockRegistry
    }
}