package tk.vnvna.sodini.discord.helpers;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.Permission;

import java.lang.reflect.Method;
import java.util.List;

@Getter
@Setter
public class CommandProperties {
  private Method commandMethod;
  private CommandBase commandGroup;
  private String matchString;
  private List<Permission> botPermissions;
  private List<Permission> userPermissions;
}
