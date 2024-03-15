package ru.aps.performance.configs

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.connection.ConnectionFactory

@Configuration
class RabbitMqConfig() {
    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin {
        val rabbitAdmin = RabbitAdmin(connectionFactory)
        return rabbitAdmin
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        return rabbitTemplate
    }
}