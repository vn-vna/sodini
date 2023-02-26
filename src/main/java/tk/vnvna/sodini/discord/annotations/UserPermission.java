package tk.vnvna.sodini.discord.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.dv8tion.jda.api.Permission;

@Retention(RetentionPolicy.RUNTIME)
public @interface UserPermission {
  Permission[] value() default {};
}
