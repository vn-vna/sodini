package tk.vnvna.sodini.exceptions;

import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;

public class UserPermissionMismatchExecption extends PermissionMismatchException {

  public UserPermissionMismatchExecption(User user, ExecutionInfo executionInfo, List<Permission> permissionMissing) {
    super(user, executionInfo, permissionMissing);
  }

}
