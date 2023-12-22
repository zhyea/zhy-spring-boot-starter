package org.chobit.spring.log;

import org.chobit.spring.OperationInvoker;
import org.chobit.spring.utils.JsonKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * QuickLog切面相关支持类
 *
 * @author rui.zhang
 */
public abstract class QuickLogAspectSupport {


    /**
     * The inner class separator character: {@code '$'}.
     */
    private static final String INNER_CLASS_SEPARATOR = "$";




    protected Object execute(final OperationInvoker invoker, final Object target, Method method, Object[] args) {
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);

        QuickLogOperation opt = getLogOperation(method, targetClass);
        if (null == opt) {
            return invoker.invoke();
        }

        // 提取参数信息
        takeArgs(method, args, opt);

        // 成本不太高，slf4j有作缓存
        final Logger logger = LoggerFactory.getLogger(targetClass);

        if (args.length > 0) {
            logger.info("{}#{} {} {}", opt.getClassName(), opt.getMethodName(), opt.getBiz(), argStr(opt.getArgs()));
        } else {
            logger.info("{}#{} {}", opt.getClassName(), opt.getMethodName(), opt.getBiz());
        }

        try {
            Object result = invoker.invoke();

            if (!Void.class.equals(method.getReturnType())) {
                logger.info("{}#{} {} 返回值:{}", opt.getClassName(), opt.getMethodName(), opt.getBiz(), JsonKit.toJson(result));
            }

            return result;

        } catch (OperationInvoker.WrappedThrowableException wte) {
            if (args.length > 0) {
                if (opt.isAllowExcept()) {
                    logger.info("{}#{} {}执行中出现异常 {}", opt.getClassName(), opt.getMethodName(), opt.getBiz(), argStr(opt.getArgs()), wte.getOriginal());
                } else {
                    logger.error("{}#{} {}执行中出现异常 {}", opt.getClassName(), opt.getMethodName(), opt.getBiz(), argStr(opt.getArgs()), wte.getOriginal());
                }
            } else {
                if (opt.isAllowExcept()) {
                    logger.info("{}#{} {}执行中出现异常", opt.getClassName(), opt.getMethodName(), opt.getBiz(), wte.getOriginal());
                } else {
                    logger.error("{}#{} {}执行中出现异常", opt.getClassName(), opt.getMethodName(), opt.getBiz(), wte.getOriginal());
                }
            }
            throw wte;
        }
    }


    /**
     * 提取参数信息
     *
     * @param method 方法
     * @param args   参数
     * @param opt    日志参数
     */
    private void takeArgs(Method method, Object[] args, QuickLogOperation opt) {
        if (null == args || args.length == 0) {
            return;
        }

        Parameter[] params = method.getParameters();
        for (int i = 0; i < args.length; i++) {
            String argName = params[i].getName();
            Object argVal = args[i];

            if (!ClassUtils.isPrimitiveOrWrapper(argVal.getClass()) && !(argVal instanceof String)) {
                argVal = JsonKit.toJson(argVal);
            }
            opt.getArgs().put(argName, argVal);
        }
    }


    /**
     * 解析日志相关属性
     *
     * @param method 方法
     * @return 日志相关属性
     */
    private QuickLogOperation getLogOperation(Method method, Class<?> targetClass) {
        if (method.getDeclaringClass() == Object.class) {
            return null;
        }

        QuickLogOperation opt = parseLogOperation(method, targetClass);
        if (null != opt) {
            return opt;
        }

        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        if (specificMethod != method) {
            opt = parseLogOperation(specificMethod, userClass);
            return opt;
        }
        return null;
    }


    /**
     * 解析日志相关属性
     *
     * @param method 方法
     * @param clazz  类
     * @return 日志相关属性
     */
    private QuickLogOperation parseLogOperation(Method method, Class<?> clazz) {
        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(method, QuickLog.class);
        if (null == attributes) {
            return null;
        }

        QuickLogOperation opt = parseLogOperation(attributes);
        opt.setMethodName(method.getName());

        String className = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
        if (className.startsWith(INNER_CLASS_SEPARATOR)) {
             className = clazz.getName();
        }
        opt.setClassName(className);

        return opt;
    }


    /**
     * 解析日志相关属性
     *
     * @param attributes 注解信息
     * @return 日志相关属性
     */
    private QuickLogOperation parseLogOperation(AnnotationAttributes attributes) {
        String biz = attributes.getString("biz");
        boolean allowExcept = attributes.getBoolean("allowExcept");

        QuickLogOperation opt = new QuickLogOperation();
        opt.setBiz(biz);
        opt.setAllowExcept(allowExcept);

        return opt;
    }


    /**
     * 参数字符串
     *
     * @param args 参数集合
     * @return 参数字符串
     */
    private String argStr(Map<String, Object> args) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> e : args.entrySet()) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(e.getKey()).append(":").append(e.getValue());
        }
        return builder.toString();
    }
}
