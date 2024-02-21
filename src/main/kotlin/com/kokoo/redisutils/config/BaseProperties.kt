package com.kokoo.redisutils.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.context.annotation.Configuration

@Configuration
class BaseProperties {

    @ConfigurationProperties(prefix = "property.pub-sub")
    @ConfigurationPropertiesBinding
    class PubSub {
        var topicName: String = ""
    }

}