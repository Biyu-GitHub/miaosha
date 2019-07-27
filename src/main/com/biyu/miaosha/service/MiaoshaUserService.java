package com.biyu.miaosha.service;

import com.biyu.miaosha.dao.MiaoshaUserDao;
import com.biyu.miaosha.entity.MiaoshaUser;
import com.biyu.miaosha.exception.GlobalException;
import com.biyu.miaosha.result.CodeMsg;
import com.biyu.miaosha.util.MD5Util;
import com.biyu.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {

    @Autowired
    MiaoshaUserDao miaoshaUserDao;


    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    public MiaoshaUser getById(long id) {
        return miaoshaUserDao.getById(id);
    }

    /**
     * 用户登录功能实现
     * 1. 判断手机号是否存在
     * 2. 获取密码一数据库中的随机码做MD5，
     *
     * @param response
     * @param loginVo
     * @return
     */
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        return true;
    }
}
