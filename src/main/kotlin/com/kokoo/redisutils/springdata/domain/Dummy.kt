package com.kokoo.redisutils.springdata.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "dummy", timeToLive = 60)
data class Dummy(
    @Id
    val id: String,
    val field: String
)