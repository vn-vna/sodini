package tk.vnvna.sodini.modules.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.discord.annotations.BotPermission;
import tk.vnvna.sodini.discord.annotations.CommandGroup;
import tk.vnvna.sodini.discord.annotations.CommandMethod;
import tk.vnvna.sodini.discord.annotations.UserPermission;
import tk.vnvna.sodini.discord.helpers.CommandBase;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;
import tk.vnvna.sodini.modules.JDAHandler;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

@AppModule
@CommandGroup
public class ManagerCommands extends CommandBase {

  @Dependency
  private JDAHandler jdaHandler;

  @Dependency
  private Logger logger;

  @BotPermission({Permission.ADMINISTRATOR})
  @UserPermission({Permission.ADMINISTRATOR})
  @CommandMethod("timeout")
  public void commandBanUser(ExecutionInfo executionInfo, @Nonnull User user, @Nonnull Integer time) {
    var event = executionInfo.getTriggerEvent();

    if (event instanceof MessageReceivedEvent mre) {
      mre.getGuild().timeoutFor(user, time, TimeUnit.MINUTES).queue();
    }
  }

  @BotPermission({Permission.ADMINISTRATOR})
  @UserPermission({Permission.ADMINISTRATOR})
  @CommandMethod("untimeout")
  public void commandUnbanUser(ExecutionInfo executionInfo, @Nonnull User user) {
    var event = executionInfo.getTriggerEvent();

    if (event instanceof MessageReceivedEvent mre) {
      mre.getGuild().removeTimeout(user).queue();
    }
  }

}
