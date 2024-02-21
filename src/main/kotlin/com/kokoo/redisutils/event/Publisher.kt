package com.kokoo.redisutils.event

import com.kokoo.redisutils.config.BaseProperties
import com.kokoo.redisutils.dto.PubSubDto
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class Publisher(
    private val pubSubRedisTemplate: RedisTemplate<String, PubSubDto>,
    private val pubSubProperties: BaseProperties.PubSub
) {

    fun publish(pubSubDto: PubSubDto) {
        pubSubRedisTemplate.convertAndSend(pubSubProperties.topicName, pubSubDto.message)
    }
}