# 简介

一些SpringBoot starter集合。

# @QuickLog

在方法上添加`@QuickLog`注解，可以在方法执行前后自动输出日志。效果如下：

```text
类名#方法名 业务描述 arg1:a, arg2:b, arg3:c

类名#方法名 业务描述 返回值:返回值json
```

# @TraceClew

为一些后端发起的任务填充traceId，目的是解决因为没有traceId造成的无法跟踪日志的问题。

