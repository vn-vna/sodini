package tk.vnvna.sodini.module.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import tk.vnvna.sodini.controller.annotations.AppModule;
import tk.vnvna.sodini.controller.annotations.Dependency;
import tk.vnvna.sodini.discord.annotations.CommandGroup;
import tk.vnvna.sodini.discord.annotations.CommandMethod;
import tk.vnvna.sodini.discord.helper.CommandBase;
import tk.vnvna.sodini.discord.helper.ExecutionInfo;
import tk.vnvna.sodini.module.JDAHandler;

@AppModule
@CommandGroup
public class PublicCommands extends CommandBase {

  @Dependency
  private JDAHandler jdaHandler;

  @Dependency
  private Logger logger;

  @CommandMethod("ping")
  public void ping(ExecutionInfo executionInfo) {
    var triggerEvent = executionInfo.getTriggerEvent();
    if (triggerEvent instanceof MessageReceivedEvent mre) {
      mre.getGuildChannel().sendMessage("pong").queue();
    }
  }

  @CommandMethod("test")
  public void test(ExecutionInfo executionInfo, String someString) {
    logger.info(someString);
  }

  @CommandMethod("channel")
  public void channel(ExecutionInfo executionInfo, Channel channel) {
    var triggerEvent = executionInfo.getTriggerEvent();
    if (triggerEvent instanceof MessageReceivedEvent mre) {
      mre.getGuildChannel().sendMessage(channel.getAsMention()).queue();
    }
  }

  @CommandMethod("user")
  public void user(ExecutionInfo executionInfo, User user) {
    var triggerEvent = executionInfo.getTriggerEvent();
    if (triggerEvent instanceof MessageReceivedEvent mre) {
      mre.getGuildChannel().sendMessage(user.getAsMention()).queue();
    }
  }

}
