package com.biyu.miaosha.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biyu.miaosha.redis.RedisService;
import com.biyu.miaosha.result.Result;
import com.biyu.miaosha.service.MiaoshaUserService;
import com.biyu.miaosha.vo.LoginVo;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    /**
     * 跳转至login页面
     * 因为配置了thymeleaf的前置为classpath:/templates/
     * 后置为.html
     * 所以return login会变成-->login.html, 并去路径下寻找
     *
     * @return
     */
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    /**
     * 执行login方法
     *
     * @param response
     * @param loginVo
     * @return
     */
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        userService.login(response, loginVo);
        return Result.success(true);
    }
}
