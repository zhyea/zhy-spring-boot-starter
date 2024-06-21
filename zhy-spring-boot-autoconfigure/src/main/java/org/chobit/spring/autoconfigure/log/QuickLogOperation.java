package org.chobit.spring.autoconfigure.log;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志相关字段
 *
 * @author robin
 */
public class QuickLogOperation {


    /**
     * 业务描述
     */
    private String biz;

    /**
     * 是否允许异常
     * <p>
     * 出现异常时是否打印错误日志
     */
    private boolean allowExcept;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 类名
     */
    private String className;

    /**
     * 参数信息
     */
    private final Map<String, Object> args = new HashMap<>(8);


    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public boolean isAllowExcept() {
        return allowExcept;
    }

    public void setAllowExcept(boolean allowExcept) {
        this.allowExcept = allowExcept;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, Object> getArgs() {
        return args;
    }


}
