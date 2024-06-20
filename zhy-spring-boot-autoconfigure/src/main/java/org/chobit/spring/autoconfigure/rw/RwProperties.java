package org.chobit.spring.rw;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置映射类
 *
 * @author robin
 */
@ConfigurationProperties(prefix = "rw")
public class RwProperties {


	/**
	 * 是否启用
	 */
	private boolean enable = true;


	/**
	 * 接口默认成功code
	 */
	private int successCode = 0;


	/**
	 * 接口默认失败code
	 */
	private int failCode = 10000;


	/**
	 * 是否开启静默模式
	 * true 不需要wrapper注解
	 * false  需要注解
	 */
	private boolean silentMode = false;


	/**
	 * 是否包装异常
	 */
	private boolean wrapExcept = true;


	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}


	public int getSuccessCode() {
		return successCode;
	}

	public void setSuccessCode(int successCode) {
		this.successCode = successCode;
	}

	public int getFailCode() {
		return failCode;
	}

	public void setFailCode(int failCode) {
		this.failCode = failCode;
	}

	public boolean isSilentMode() {
		return silentMode;
	}

	public void setSilentMode(boolean silentMode) {
		this.silentMode = silentMode;
	}

	public boolean isWrapExcept() {
		return wrapExcept;
	}

	public void setWrapExcept(boolean wrapExcept) {
		this.wrapExcept = wrapExcept;
	}
}
