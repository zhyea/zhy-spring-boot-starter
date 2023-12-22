package org.chobit.spring.trace;

import org.springframework.beans.factory.InitializingBean;

/**
 * TraceClew Starter
 *
 * @author rui.zhang
 */
public class TraceClewStarter implements InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("------------------->>>> TraceClew has been enabled!");
    }
}
