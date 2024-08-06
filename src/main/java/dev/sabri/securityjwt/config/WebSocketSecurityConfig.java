package dev.sabri.securityjwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static org.springframework.messaging.simp.stomp.StompHeaderAccessor.*;

@Configuration
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                assert accessor != null;
                if (StompHeaderAccessor.CONNECT_MESSAGE_HEADER.equals(accessor.getCommand())) {
                    // Get the token from session attributes
                    String token = (String) accessor.getSessionAttributes().get("token");
                    if (token != null) {
                        // Authenticate the token
                        Authentication authentication = getAuthentication(token);
                        if (authentication != null) {
                            accessor.setUser(authentication);
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
                return message;
            }
        });
    }

    private Authentication getAuthentication(String token) {
        // Implement your token validation and authentication logic here
        // For example, you can use a JWT utility class to validate the token and retrieve the user details
        // Return an Authentication object if the token is valid
        return null;
    }
}
