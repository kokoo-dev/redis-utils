package com.kokoo.redisutils.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.context.annotation.Configuration

@Configuration
class BaseProperties {

    @ConfigurationProperties(prefix = "property.pub-sub")
    @ConfigurationPropertiesBinding
    data class PubSub (
        var topicName: String = ""
    )

    @ConfigurationProperties(prefix = "property.lock-registry")
    @ConfigurationPropertiesBinding
    data class LockRegistry (
        var key: String = "",
        var lockExpireSeconds: Long = 5
    )
}