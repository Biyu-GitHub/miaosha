package com.biyu.miaosha.controller;

import com.biyu.miaosha.entity.User;
import com.biyu.miaosha.result.CodeMsg;
import com.biyu.miaosha.result.Result;
import com.biyu.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    UserService userService;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    //1.rest api json输出 2.页面
    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
        // return new Result(0, "success", "hello,imooc");
        return Result.success("hello,biyu");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError() {
        //return new Result(500102, "XXX");
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/thymeleaf")
    public String  thymeleaf(Model model) {
        model.addAttribute("name", "biyu");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }
}
