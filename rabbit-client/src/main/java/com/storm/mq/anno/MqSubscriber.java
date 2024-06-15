package com.storm.mq.anno;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MqSubscriber {

    String[] topics();

    String clientName() default "";

    String group() default "";

    boolean share() default false;
}
