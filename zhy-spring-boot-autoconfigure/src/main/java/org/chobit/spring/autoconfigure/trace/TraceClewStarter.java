package org.chobit.spring.autoconfigure.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * TraceClew Starter
 *
 * @author robin
 */
public class TraceClewStarter implements InitializingBean {


    private static final Logger logger = LoggerFactory.getLogger(TraceClewStarter.class);


    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("TraceClew has been enabled.");
    }
}
