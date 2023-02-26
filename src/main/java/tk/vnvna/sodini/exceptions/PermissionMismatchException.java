package tk.vnvna.sodini.exceptions;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;

@Getter
@AllArgsConstructor
public class PermissionMismatchException extends RuntimeException {
  protected User user;
  protected ExecutionInfo executionInfo;
  protected List<Permission> permissionMissing;
}
