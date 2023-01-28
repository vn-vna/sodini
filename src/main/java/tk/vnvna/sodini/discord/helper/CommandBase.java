package tk.vnvna.sodini.discord.helper;

import lombok.Getter;
import tk.vnvna.sodini.discord.annotations.CommandGroup;
import tk.vnvna.sodini.utils.StringUtils;

import java.util.Objects;

public abstract class CommandBase {
  @Getter
  protected String groupMatcher;

  public CommandBase() {
    var groupAnnotation = this.getClass().getAnnotation(CommandGroup.class);
    if (Objects.nonNull(groupAnnotation)) {
      groupMatcher = StringUtils.joinNonBlanks(" ", groupAnnotation.value());
    } else {
      groupMatcher = "";
    }
  }

}
