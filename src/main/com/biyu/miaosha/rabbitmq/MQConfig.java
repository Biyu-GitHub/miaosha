package com.biyu.miaosha.rabbitmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class MQConfig {

    // 定义消息队列的名字
    public static final String QUEUE = "queue";

    /**
     * Derict 模式
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }
}
