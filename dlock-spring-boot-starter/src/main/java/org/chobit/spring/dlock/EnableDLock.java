package org.chobit.spring.dlock;


import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * kafka stater annotation
 *
 * @author robin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(DLockConfiguration.class)
public @interface EnableDLock {


}
