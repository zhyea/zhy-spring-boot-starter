package org.chobit.spring.rw;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置映射类
 *
 * @author robin
 */
@ConfigurationProperties(prefix = "rw")
public class RwProperties {

    
    private boolean enabled;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
