package app.core.anotation;

import app.core.http.enums.MethodTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


import java.lang.annotation.*;

import static app.core.http.enums.MethodTypes.GET;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Controller {
    MethodTypes method() default GET;

    String urlPatten() default "/*";

    InitParameter[] initParameters() default {};
}