package tterrag.supermassivetech.handlers;

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
    
    HandlerType[] types();
}
