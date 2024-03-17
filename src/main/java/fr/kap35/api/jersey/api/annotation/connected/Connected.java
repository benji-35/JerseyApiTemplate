package fr.kap35.api.jersey.api.annotation.connected;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.ws.rs.core.Response.Status;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Connected {
    Status returned() default Status.UNAUTHORIZED;
    String property() default "Authorization";
}
