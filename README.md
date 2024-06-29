# 简介

一些SpringBoot starter集合。

# @QuickLog

在方法上添加`@QuickLog`注解，可以在方法执行前后自动输出日志。效果如下：

```text
类名#方法名 业务描述 arg1:a, arg2:b, arg3:c

类名#方法名 业务描述 返回值:返回值json
```

# @TraceClew

通过在方法上添加`@TraceClew`为一些后端发起的方法填充traceId，以解决因为没有traceId造成的无法跟踪方法执行日志的问题。


# @ResponseWrapper

引入当前依赖后，在接口方法或接口类上添加`@ResponseWrapper`注解，即可完成对返回值的封装。

> 接口类指存在`@RestController`注解的类  
> 接口方法指存在`@RestController`注解的类下的方法，或者存在`@ResponseBody`注解的方法


# @RLock

基于redis实现的分布式锁，直接复用了redis的配置，因此要使用RLock分布式锁需要先添加redis的配置。

启用RLock分布式锁需要在启动类上添加 `@EnableRLock` 注解。

启用后，需要使用分布式锁时直接在方法上添加`@RLock`注解，然后注解文档完成配置即可。


