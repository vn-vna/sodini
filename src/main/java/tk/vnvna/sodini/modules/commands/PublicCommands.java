package tk.vnvna.sodini.modules.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.discord.annotations.CommandGroup;
import tk.vnvna.sodini.discord.annotations.CommandMethod;
import tk.vnvna.sodini.discord.helpers.CommandBase;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;
import tk.vnvna.sodini.modules.Configuration;
import tk.vnvna.sodini.modules.JDAHandler;
import tk.vnvna.sodini.modules.KitsuApiClient;
import tk.vnvna.sodini.utils.DateTimeUtils;
import tk.vnvna.sodini.utils.TimeUtils;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@AppModule
@CommandGroup
public class PublicCommands extends CommandBase {

  @Dependency
  private JDAHandler jdaHandler;

  @Dependency
  private KitsuApiClient kitsuApiClient;

  @Dependency
  private Configuration configuration;

  @Dependency
  private Logger logger;

  @CommandMethod("ping")
  public void commandPing(ExecutionInfo executionInfo) {
    var triggerEvent = executionInfo.getTriggerEvent();
    if (triggerEvent instanceof MessageReceivedEvent mre) {
      mre.getGuildChannel()
        .sendMessage("Gateway ping: " + jdaHandler.getJda().getGatewayPing() + " ms")
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

  @CommandMethod("member")
  public void commandShowMemberInfo(ExecutionInfo executionInfo, @Nonnull User user) {
    var triggerEvent = executionInfo.getTriggerEvent();

    if (triggerEvent instanceof MessageReceivedEvent mre) {
      var member = mre.getGuild().getMember(user);

      var rolesMentioned = member
        .getRoles()
        .stream()
        .map((r) -> r.getAsMention())
        .reduce("", (s, r) -> s + r + ' ');

      var dateTimeFormatter = DateTimeUtils.NORMAL_FORMAT;

      var embed = new EmbedBuilder()
        .setTitle(member.getEffectiveName())
        .setDescription("ID: " + member.getId())
        .setColor(member.getColor())
        .addField("Joined", member.getTimeJoined().format(dateTimeFormatter), false)
        .addField("Account created", user.getTimeCreated().format(dateTimeFormatter), false)
        .addField("Username", member.getUser().getAsTag(), false)
        .addField("Avatar URL", member.getEffectiveAvatarUrl(), false)
        .addField("Roles", rolesMentioned, false)
        .build();

      var message = new MessageCreateBuilder()
        .setEmbeds(embed)
        .build();

      mre.getChannel()
        .sendMessage(message)
        .setAllowedMentions(Set.of())
        .queue();
    }
  }


  @CommandMethod("trending-manga")
  public void commandShowMangaTrending(ExecutionInfo executionInfo) {

    var titleOrder = configuration.getListConfiguration("Application::Api::KitsuTitleOrder::Value");

    var embedBuilder = new EmbedBuilder()
      .setTitle("Trending Manga")
      .setDescription("Latest trending manga list from Kitsu API")
      .setTimestamp(TimeUtils.getDateTimeUTC());

    var data = kitsuApiClient.getTrendingManga()
      .getData();

    data.forEach((attrib) -> {
      var titles = attrib.getAttributes().getTitles();

      String title = null;
      for (var lang : titleOrder) {
        title = titles.get(lang);

        if (Objects.nonNull(title)) {
          break;
        }
      }

      if (Objects.isNull(title)) {
        for (var titleSet : titles.entrySet()) {
          title = titleSet.getValue();

          if (Objects.nonNull(title)) {
            break;
          }
        }
      }

      if (Objects.isNull(title)) {
        title = "Manga";
      }

      var synopsis = attrib.getAttributes().getSynopsis();

      if (Objects.nonNull(synopsis) && synopsis.length() > 50) {
        synopsis = synopsis.substring(0, 50);
        synopsis += "...";
      }

      var synopsisOp = Optional.of(synopsis);

      embedBuilder.addField(
        title,
        synopsisOp.orElse("Synopsis"),
        false);
    });

    var msg = new MessageCreateBuilder()
      .setEmbeds(embedBuilder.build())
      .build();

    var event = executionInfo.getTriggerEvent();

    if (event instanceof MessageReceivedEvent mre) {
      mre.getChannel()
        .sendMessage(msg)
        .queue();
    }
  }


  @CommandMethod("trending-ani")
  public void commandShowAnimeTrending(ExecutionInfo executionInfo) {

    var titleOrder = configuration.getListConfiguration("Application::Api::KitsuTitleOrder::Value");

    var embedBuilder = new EmbedBuilder()
      .setTitle("Trending Anime")
      .setDescription("Latest trending anime list from Kitsu API")
      .setTimestamp(TimeUtils.getDateTimeUTC());

    var data = kitsuApiClient.getTrendingAnime()
      .getData();

    data.forEach((attrib) -> {
      var titles = attrib.getAttributes().getTitles();

      String title = null;
      for (var lang : titleOrder) {
        title = titles.get(lang);

        if (Objects.nonNull(title)) {
          break;
        }
      }

      if (Objects.isNull(title)) {
        for (var titleSet : titles.entrySet()) {
          title = titleSet.getValue();

          if (Objects.nonNull(title)) {
            break;
          }
        }
      }

      if (Objects.isNull(title)) {
        title = "Anime";
      }

      var synopsis = attrib.getAttributes().getSynopsis();

      if (Objects.nonNull(synopsis) && synopsis.length() > 50) {
        synopsis = synopsis.substring(0, 50);
        synopsis += "...";
      }

      var synopsisOp = Optional.of(synopsis);

      embedBuilder.addField(
        title,
        synopsisOp.orElse("Synopsis"),
        false);
    });

    var msg = new MessageCreateBuilder()
      .setEmbeds(embedBuilder.build())
      .build();

    var event = executionInfo.getTriggerEvent();

    if (event instanceof MessageReceivedEvent mre) {
      mre.getChannel()
        .sendMessage(msg)
        .queue();
    }
  }
}
