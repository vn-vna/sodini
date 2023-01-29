package tk.vnvna.sodini.modules.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.discord.annotations.CommandGroup;
import tk.vnvna.sodini.discord.annotations.CommandMethod;
import tk.vnvna.sodini.discord.helpers.CommandBase;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;
import tk.vnvna.sodini.modules.JDAHandler;
import tk.vnvna.sodini.utils.ApiUtils;

import javax.annotation.Nonnull;

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
      mre.getGuildChannel()
          .sendMessage("pong")
          .queue();
    }
  }

  @CommandMethod("test")
  public void test(ExecutionInfo executionInfo, @Nonnull String someString) {
    logger.info(someString);
  }

  @CommandMethod("channel")
  public void channel(ExecutionInfo executionInfo, @Nonnull GuildMessageChannel channel) {
    var triggerEvent = executionInfo.getTriggerEvent();
    if (triggerEvent instanceof MessageReceivedEvent mre) {
      mre.getGuildChannel()
          .sendMessage(channel.getAsMention())
          .queue();
    }
  }

  @CommandMethod("user")
  public void user(ExecutionInfo executionInfo, User user) {
    var triggerEvent = executionInfo.getTriggerEvent();
    if (triggerEvent instanceof MessageReceivedEvent mre) {
      mre.getGuildChannel()
          .sendMessage(user.getAsMention())
          .queue();
    }
  }

  @CommandMethod("te")
  public void trendingAnimes(ExecutionInfo executionInfo) {
    ApiUtils.getTrendingAnimes();
  }

}