package tk.vnvna.sodini.discord.annotations;

import net.dv8tion.jda.api.Permission;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UserPermission {
  Permission[] value() default {};
}
