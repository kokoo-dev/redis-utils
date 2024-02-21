package com.kokoo.redisutils.event

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component

@Component
class Subscriber : MessageListener {

    private val log = KotlinLogging.logger {}

    override fun onMessage(message: Message, pattern: ByteArray?) {
        log.info { "pattern :: ${String(pattern ?: ByteArray(0))}, message:: $message" }
    }
}