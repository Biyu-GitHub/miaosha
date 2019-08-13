package com.biyu.miaosha.rabbitmq;

import com.biyu.miaosha.entity.MiaoshaOrder;
import com.biyu.miaosha.entity.MiaoshaUser;
import com.biyu.miaosha.entity.OrderInfo;
import com.biyu.miaosha.redis.RedisService;
import com.biyu.miaosha.result.CodeMsg;
import com.biyu.miaosha.service.GoodsService;
import com.biyu.miaosha.service.MiaoshaService;
import com.biyu.miaosha.service.OrderService;
import com.biyu.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;


    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    /**
     * 接收消息，进行减库存与下订单操作
     *
     * @param message
     */
    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        logger.info("receive:" + message);
        MiaoshaMessage miaoshaMessage = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = miaoshaMessage.getUser();
        long goodsId = miaoshaMessage.getGoodsId();

        // 访问数据库判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }

        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }

        // 生成秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
    }
}
