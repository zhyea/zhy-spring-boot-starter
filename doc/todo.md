
TODO 事项

1. 添加分布式锁集合注解 @RLockList, 实现在调用一个方法时能够获得多把锁
2. 完善rw组件异常拦截机制，对注解校验产生的异常进行优化， [参考文档](https://stackoverflow.com/questions/7109296/bind-global-errors-generated-from-form-validation-to-specific-form-fields-in-spr)
3. 在消费消息时，如果长时间消费不到消息，sleep一段时间，避免过于频繁的请求redis
4. 添加LUA脚本，减少与redis的交互次数
5. 在redisq启动时，不再用true/false来判断是否开启redisq，而是使用一个枚举类来表示是否开启redisq



