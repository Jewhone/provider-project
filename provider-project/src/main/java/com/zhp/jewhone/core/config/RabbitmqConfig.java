package com.zhp.jewhone.core.config;

import com.zhp.jewhone.core.constant.enums.RabbitmqEnum;
import com.zhp.jewhone.core.util.rabbitmq.MsgSendConfirmCallBack;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    @Value("${spring.rabbitmq.host}")
    public String host;
    @Value("${spring.rabbitmq.port}")
    public int port;
    @Value("${spring.rabbitmq.username}")
    public String username;
    @Value("${spring.rabbitmq.password}")
    public String password;
    @Value("${spring.rabbitmq.virtual-host}")
    public String virtualHost;


    /**
     * 创建连接工厂
     */
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.host, this.port);
        connectionFactory.setUsername(this.username);
        connectionFactory.setPassword(this.password);
        connectionFactory.setVirtualHost(this.virtualHost);
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }

    /**
     * 1.定义direct exchange，绑定queueTest
     * 2.durable="true" rabbitmq重启的时候不需要创建新的交换机
     * 3.direct交换器相对来说比较简单，匹配规则为：如果路由键匹配，消息就被投送到相关的队列
     * fanout交换器中没有路由键的概念，他会把消息发送到所有绑定在此交换器上面的队列中。
     * topic交换器你采用模糊匹配路由键的原则进行转发消息到队列中
     * key: queue在该direct-exchange中的key值，当消息发送给direct-exchange中指定key为设置值时，消息将会转发给queue参数指定
     * 的消息队列
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(RabbitmqEnum.MOBILE_LOG.getExchange(), true, false);
    }

    /**
     * durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
     * auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
     * exclusive  表示该消息队列是否只在当前connection生效,默认是false
     */
    @Bean
    public Queue queue_one() {
        return new Queue(RabbitmqEnum.MOBILE_LOG.getQueue(), true, false, false);
    }

    /**
     * 将消息队列1和交换机进行绑定
     */
    @Bean
    public Binding binding_one() {
        return BindingBuilder.bind(queue_one()).to(directExchange()).with(RabbitmqEnum.MOBILE_LOG.getRoutingKey(RabbitmqEnum.MOBILE_LOG));
    }

    /**
     * 定义rabbit template用于数据的接收和发送
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setConfirmCallback(msgSendConfirmCallBack());
        return template;
    }

    /**
     * 消息确认机制
     * Confirms给客户端一种轻量级的方式，能够跟踪哪些消息被broker处理，哪些可能因为broker宕掉或者网络失败的情况而重新发布。
     * 确认并且保证消息被送达，提供了两种方式：发布确认和事务。(两者不可同时使用)在channel为事务时，
     * 不可引入确认模式；同样channel为确认模式下，不可使用事务。
     */
    @Bean
    public MsgSendConfirmCallBack msgSendConfirmCallBack() {
        return new MsgSendConfirmCallBack();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);//设置确认模式手动确认
        return factory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter());
    }
}
