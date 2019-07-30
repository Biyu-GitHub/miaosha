# 秒杀压测

使用5000个线程，模拟用户，每个用户访问10次，QPS为100左右

## 进行压测的时候暴露出来的问题

* 库存变为复数：

  当前的秒杀流程是：

  * 判断库存是否大于0
  * 判断该用户是否重复下单
  * 减库存

  所以当资源为1的时候，多个线程同时进行秒杀的时候就出现了问题