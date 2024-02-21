package com.kokoo.redisutils

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedisPubsubApplication

fun main(args: Array<String>) {

	runApplication<RedisPubsubApplication>(*args)
}
