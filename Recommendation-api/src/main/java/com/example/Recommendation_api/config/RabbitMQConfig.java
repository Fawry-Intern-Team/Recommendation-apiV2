package com.example.Recommendation_api.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_BEHAVIOR_QUEUE = "user.behavior.queue";
    public static final String USER_BEHAVIOR_EXCHANGE = "user.behavior.exchange";
    public static final String USER_BEHAVIOR_ROUTING_KEY = "user.behavior.routing";

    @Bean
    public Queue userBehaviorQueue() {
        return QueueBuilder.durable(USER_BEHAVIOR_QUEUE).build();
    }

    @Bean
    public TopicExchange userBehaviorExchange() {
        return new TopicExchange(USER_BEHAVIOR_EXCHANGE);
    }

    @Bean
    public Binding userBehaviorBinding() {
        return BindingBuilder
                .bind(userBehaviorQueue())
                .to(userBehaviorExchange())
                .with(USER_BEHAVIOR_ROUTING_KEY);
    }
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
