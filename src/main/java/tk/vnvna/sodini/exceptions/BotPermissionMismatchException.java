package tk.vnvna.sodini.exceptions;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;

import java.util.List;

public class BotPermissionMismatchException extends PermissionMismatchException {
  public BotPermissionMismatchException(User user, ExecutionInfo executionInfo, List<Permission> permissionMissing) {
    super(user, executionInfo, permissionMissing);
  }
}
