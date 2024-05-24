package ru.aps.performance.configs

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
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
        rabbitTemplate.setMessageConverter(converter())
        return rabbitTemplate
    }

    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin {
        val rabbitAdmin = RabbitAdmin(connectionFactory)
        rabbitAdmin.declareExchange(exchange())
        return rabbitAdmin;
    }

    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange("performance-exchange")
    }

    @Bean
    fun converter(): Jackson2JsonMessageConverter {
        val converter = Jackson2JsonMessageConverter()
        return converter
    }

    companion object {
        val logger = LogManager.getLogger()
    }
}