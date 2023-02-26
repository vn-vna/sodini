package tk.vnvna.sodini.exceptions;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;

public class BotPermissionMismatchException extends PermissionMismatchException {
  public BotPermissionMismatchException(User user, ExecutionInfo executionInfo, List<Permission> permissionMissing) {
    super(user, executionInfo, permissionMissing);
  }
}
