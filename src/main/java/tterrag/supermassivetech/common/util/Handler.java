package tterrag.supermassivetech.common.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler
{
    public enum HandlerType {
        FORGE,
        FML
    }
    
    HandlerType[] types() default {HandlerType.FORGE, HandlerType.FML};
}
