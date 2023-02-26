package tk.vnvna.sodini.modules.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.discord.annotations.BotPermission;
import tk.vnvna.sodini.discord.annotations.CommandGroup;
import tk.vnvna.sodini.discord.annotations.CommandMethod;
import tk.vnvna.sodini.discord.helpers.CommandBase;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;
import tk.vnvna.sodini.modules.JDAHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

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
          .sendMessage("Gateway ping: " + jdaHandler.getJda().getGatewayPing() + " ms")
          .queue();
    }
  }

  @CommandMethod("user")
  public void user(
      ExecutionInfo executionInfo,
      User user) {
    var triggerEvent = executionInfo.getTriggerEvent();
    if (triggerEvent instanceof MessageReceivedEvent mre) {
      mre.getGuildChannel()
          .sendMessage(user.getAsMention())
          .queue();
    }
  }

  @CommandMethod("avatar")
  public void commandShowAvatar(ExecutionInfo executionInfo, @Nonnull User user) {
    var triggerEvent = executionInfo.getTriggerEvent();

    if (triggerEvent instanceof MessageReceivedEvent mre) {
      var member = mre.getGuild().getMember(user);
      var userAvatar = user.getAvatarUrl();
      var guildAvatar = member.getAvatarUrl();


      var messageBuilder = new MessageCreateBuilder();

      if (Objects.isNull(userAvatar) && Objects.isNull(guildAvatar)) {
        messageBuilder
            .setContent("User [" + member.getAsMention() + "] has no avatar to display");
      } else {
        if (Objects.nonNull(userAvatar)) {
          var embed = new EmbedBuilder()
              .setTitle("User avatar")
              .setImage(userAvatar)
              .build();

          messageBuilder
              .addEmbeds(embed);
        }

        if (Objects.nonNull(guildAvatar)) {
          var embed = new EmbedBuilder()
              .setTitle("Guild avatar")
              .setImage(guildAvatar)
              .build();

          messageBuilder
              .addEmbeds(embed);
        }
      }

      mre.getChannel()
          .sendMessage(messageBuilder.build())
          .queue();
    }
  }

}
