package tk.vnvna.sodini.discord.helper;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import tk.vnvna.sodini.app.EntryPoint;
import tk.vnvna.sodini.discord.annotations.CommandGroup;
import tk.vnvna.sodini.module.JDAHandler;
import tk.vnvna.sodini.utils.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

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

  protected JDA getJDA() {
    return EntryPoint.getInstance()
        .getModuleController()
        .getModule(JDAHandler.class)
        .getJda();
  }


}
