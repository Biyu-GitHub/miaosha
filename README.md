# 环境搭建

* junit
* spring-boot-starter-web
* spring-boot-starter-thymeleaf

# 异常状态码类

```java
/**
 * 状态错误码信息
 *
 * @code 错误码
 * @msg 错误信息
 * 定义一些静态的成员，使用的时候直接调用即可
 * 例如：CodeMsg.SERVER_ERROR
 * 重写toString方法，方便输出
 */
public class CodeMsg {
    private int code;
    private String msg;

    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
}
```

# SpringBoot

## Controller

在类外边加注解

* **@Controller**
* **@RequestMapping("/demo")** 指定该类的访问路径

```java
@Controller
@RequestMapping("/demo")
public class DemoController {}
```

在方法上加注解

* **@ResponseBody** 假如是字符串则直接将字符串写到客户端,假如是一个对象，此时会将对象转化为json串然后写到客户端

  > 如果要返回一个页面，则不要加ResponseBody注释

* **@RequestMapping("/hello")** 指定方法的访问路径

```java
@RequestMapping("/hello")
@ResponseBody
public String home() {
    return "Hello World!";
}
```

# Entity

## 1. **goods**

| 列名         | 说明                     |
| ------------ | ------------------------ |
| id           | 商品ID                   |
| goods_name   | 商品名称                 |
| goods_title  | 商品标题                 |
| goods_img    | 商品图片                 |
| goods_detail | 商品详情介绍             |
| goods_price  | 商品单价                 |
| goods_stock  | 商品库存，-1代表没有限制 |

## 2. **miaosha_goods**

| 列名          | 说明           |
| ------------- | -------------- |
| id            | 秒杀商品表的id |
| goods_id      | 对应商品的id   |
| miaosha_price | 秒杀价         |
| stock_count   | 库存数量       |
| start_date    | 秒杀开始时间   |
| end_date      | 秒杀结束时间   |

## 3. miaosha_user

| 列名            | 说明              |
| --------------- | ----------------- |
| id              | 用户ID，手机号码  |
| nickname        | 昵称              |
| password        | 两次MD5加密的密码 |
| salt            | 第二次的随机码    |
| head            | 头像，云存储的id  |
| register_data   | 注册时间          |
| last_login_data | 上次登录时间      |
| login_count     | 登录次数          |

## 4. **order_info**

| 列名             | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| id               |                                                              |
| user_id          |                                                              |
| goods_id         |                                                              |
| delivery_addr_id | 收获地址ID                                                   |
| goods_name       |                                                              |
| goods_count      | 购买数量                                                     |
| goods_price      |                                                              |
| order_channel    | 购买渠道：1-pc，2-android，3-ios                             |
| status           | 订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成 |
| create_date      | 订单的创建时间                                               |
| pay_date         | 支付时间                                                     |

## 5. **miaosha_order**

```sql
UNIQUE KEY `u_uid_gid` (`user_id`,`goods_id`) USING BTREE
```

| 列名     | 说明   |
| -------- | ------ |
| id       |        |
| user_id  | 用户ID |
| order_id | 订单ID |
| goods_id | 商品ID |

# 登录功能

## 登录页面

在提交的时候加密一次

```html
var str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
var password = md5(str);

$.ajax({
    url: "/login/do_login",
    type: "POST",
    data: {
    mobile: $("#mobile").val(),
    password: password
})
```

```java
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
```

loginVo存储手机号与一次加密的密码，在loginVo中进行手机号的校验

```java
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
    
    // 获取手机号与密码
    String mobile = loginVo.getMobile();
    String formPass = loginVo.getPassword();
    
    // 查询数据库，判断手机号是否存在
    MiaoshaUser user = getById(Long.parseLong(mobile));
    if (user == null) {
        throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
    }
    
    /**
    * 1.从数据库获取密码
    * 2.获取随机字符串
    * 3.使用该字符串加密前端传过来的密码
    * 4.判断密码是否正确
    */
    String dbPass = user.getPassword();
    String saltDB = user.getSalt();
    String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
    if (!calcPass.equals(dbPass)) {
        throw new GlobalException(CodeMsg.PASSWORD_ERROR);
    }

    // 生成cookie
    String token = UUIDUtil.uuid(); // 生成随机的token
    addCookie(response, token, user);
    return true;
}
```

* 写缓存

    1. token + 前缀
    2. user转字符串
    3. 获取国企时间
    4. 将token:user写入到redis缓存
* 写cookie
    1. 将“token”:token写入到session中
    2. 必须设置过期时间

# 秒杀压测

使用5000个线程，模拟用户，每个用户访问10次，QPS为100左右

## 进行压测的时候暴露出来的问题

* 库存变为复数：

  当前的秒杀流程是：

  * 判断库存是否大于0
  * 判断该用户是否重复下单
  * 减库存

  所以当资源为1的时候，多个线程同时进行秒杀的时候就出现了问题