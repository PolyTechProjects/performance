package ru.aps.performance.configs

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.core.TopicExchange
import org.apache.logging.log4j.LogManager

@Configuration
class RabbitMqConfig() {
    @Bean
    fun connectionFactory(): ConnectionFactory {
        val connectionFactory = CachingConnectionFactory()
        connectionFactory.setHost("rabbitmq")
        connectionFactory.setPort(5672)
        connectionFactory.setVirtualHost("/")
        connectionFactory.setUsername("guest")
        connectionFactory.setPassword("guest")
        return connectionFactory
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        return rabbitTemplate
    }

    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange("performance-exchange")
    }

    companion object {
        val logger = LogManager.getLogger()
    }
}