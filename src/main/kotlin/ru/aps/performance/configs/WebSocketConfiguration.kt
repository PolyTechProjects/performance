package ru.aps.performance.configs

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.beans.factory.annotation.Value

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfiguration(
    @Value("\${relay-broker.host:localhost}")
    private val host: String
): WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        /*config.enableStompBrokerRelay("/queue")
            .setRelayHost(host)
            .setRelayPort(61613)
            .setClientLogin("guest")
            .setClientPasscode("guest")
            .setSystemLogin("guest")
            .setSystemPasscode("guest")*/
        config.enableSimpleBroker("/queue")
        config.setApplicationDestinationPrefixes("/chat")
        config.setUserDestinationPrefix("/user")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/websockets")
            .setAllowedOrigins("*")
            .withSockJS()
    }
}