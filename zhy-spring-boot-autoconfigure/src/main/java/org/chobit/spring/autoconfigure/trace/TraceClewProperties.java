package org.chobit.spring.autoconfigure.trace;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "trace")
public class TraceClewProperties {

    private String flag;


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
