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
> 接口方法指存在`@RestController`注解的类下的方法，或者存在`@ResponseBody`方法