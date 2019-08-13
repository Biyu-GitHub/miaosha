package com.biyu.miaosha.service;

import com.biyu.miaosha.entity.MiaoshaOrder;
import com.biyu.miaosha.redis.MiaoshaKey;
import com.biyu.miaosha.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biyu.miaosha.entity.MiaoshaUser;
import com.biyu.miaosha.entity.OrderInfo;
import com.biyu.miaosha.vo.GoodsVo;

@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存
        boolean success = goodsService.reduceStock(goods);

        if (success) {
            //订单写入两个表：order_info 与 maiosha_order
            return orderService.createOrder(user, goods);
        } else {
            return null;
        }
    }

    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if(order != null) {//秒杀成功
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
    }

}
