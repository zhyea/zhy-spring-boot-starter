package org.chobit.spring.rw.exception;

/**
 * 服务异常包装类
 *
 * @author robin
 */
public class RwException extends RuntimeException {


    private static final long serialVersionUID = 7773203247174587636L;


    private final int code;

    public RwException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public RwException(int code, String msg, Throwable t) {
        super(msg, t);
        this.code = code;
    }


    public int getCode() {
        return code;
    }
}
