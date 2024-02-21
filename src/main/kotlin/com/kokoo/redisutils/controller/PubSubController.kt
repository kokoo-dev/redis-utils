package com.kokoo.redisutils.controller

import com.kokoo.redisutils.dto.PubSubDto
import com.kokoo.redisutils.event.Publisher
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/pub-sub"])
class PubSubController(
    private val publisher: Publisher
) {

    @PostMapping("")
    fun publish(@RequestBody pubSubDto: PubSubDto) {
        publisher.publish(pubSubDto)
    }
}