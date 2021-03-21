package com.olive.rabbitmq.sample.configuration;

import com.olive.rabbitmq.sample.consumer.ImplementListener;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @description: RabbitTemplate实例化
 * @program: olive
 * @author: dtq
 * @create: 2021/2/8 14:16
 */
@Configuration
public class RabbitConfig {

    @Resource
    private Environment env;

    @Resource
    private ImplementListener implementListener;

    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        listenerContainerFactory.setConnectionFactory(connectionFactory());
        listenerContainerFactory.setConcurrentConsumers(10);
        listenerContainerFactory.setMaxConcurrentConsumers(15);
        listenerContainerFactory.setPrefetchCount(1);//预处理消息个数
        listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);//开启消息确认机制
        return listenerContainerFactory;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        String host = env.getProperty("spring.rabbitmq.host");
        int port = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.rabbitmq.port")));
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(Objects.requireNonNull(env.getProperty("spring.rabbitmq.username")));
        connectionFactory.setPassword(Objects.requireNonNull(env.getProperty("spring.rabbitmq.password")));
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    @Bean
    public SimpleMessageListenerContainer getListenerContainer(@Qualifier("sampleQueue") Queue queue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setQueues(queue);
        container.setMessageListener(implementListener);
        return container;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * DirectExchange: 按照routingkey分发到指定队列
     * TopicExchange: 多关键字匹配
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(env.getProperty("rabbitmq.sample.exchange"));
    }

    /**
     * 获取队列A
     *
     * @return
     */
    @Bean
    public Queue sampleQueue() {
        return new Queue(Objects.requireNonNull(env.getProperty("rabbitmq.sample.queue")), true); //队列持久
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(sampleQueue()).to(defaultExchange())
                .with(env.getProperty("rabbitmq.sample.routing.key"));
    }
}
