package com.biyu.miaosha.service;

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

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        goodsService.reduceStock(goods);
        //订单写入两个表：order_info 与 maiosha_order
        return orderService.createOrder(user, goods);
    }

}
