package tk.vnvna.sodini.modules.listeners;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.controllers.annotations.ModuleEntry;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;
import tk.vnvna.sodini.discord.helpers.ExecutionResult;
import tk.vnvna.sodini.exceptions.ArgumentListNotCompatibleException;
import tk.vnvna.sodini.exceptions.BotPermissionMismatchException;
import tk.vnvna.sodini.exceptions.UserPermissionMismatchExecption;
import tk.vnvna.sodini.modules.CommandExecutor;
import tk.vnvna.sodini.modules.CommandLoader;
import tk.vnvna.sodini.modules.CommandMatcher;
import tk.vnvna.sodini.modules.Configuration;
import tk.vnvna.sodini.utils.DiscordUtils;
import tk.vnvna.sodini.utils.TimeUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@AppModule
public class MessageListener extends ListenerAdapter {

  @Dependency
  private Configuration configuration;

  @Dependency
  private CommandExecutor commandExecutor;

  @Dependency
  private CommandLoader commandLoader;

  @Dependency
  private CommandMatcher commandMatcher;

  @Dependency
  private Logger logger;

  @Getter
  private String commandPrefix;

  @ModuleEntry
  public void initialize() {
    commandPrefix = configuration.requireConfiguration("Discord::Prefix");
  }

  @Override
  public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
    super.onMessageReceived(event);

    var rawContent = event.getMessage().getContentRaw();
    if (rawContent.startsWith(commandPrefix)) {
      var commandString = rawContent.substring(commandPrefix.length());

      commandMatcher
          .matchCommand(event, commandString)
          .ifPresent((executionInfo -> {
            try {
              var executionResult = commandExecutor.executeCommand(executionInfo).get();

              if (executionResult.isSuccess()) {
                handleCommandSuccess(executionResult);
              } else {
                handleCommandError(executionResult);
              }
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }));
    }
  }

  private void handleCommandError(ExecutionResult executionResult) {
    var executionInfo = executionResult.getExecutionInfo();
    var event = (MessageReceivedEvent) executionResult.getExecutionInfo().getTriggerEvent();

    // Show log
    logger.error(
        "Execution of command \"{}\" from guild \"{}\" requested by user \"{}\" has been cancelled due to error: {}",
        executionInfo.getCommandProperties().getMatchString(),
        event.getGuild().getName(),
        event.getAuthor().getAsTag(),
        executionResult.getException().getClass().getSimpleName());

    // Handle error
    var error = executionResult.getException();

    if (error instanceof ArgumentListNotCompatibleException ance) {
      respondANCException(event, ance, executionResult);
      return;
    }

    if (error instanceof BotPermissionMismatchException bpme) {
      respondBPMException(event, bpme, executionResult);
      return;
    }

    if (error instanceof UserPermissionMismatchExecption upme) {
      respondUPMException(event, upme, executionResult);
      return;
    }
  }

  private void respondANCException(MessageReceivedEvent mre, ArgumentListNotCompatibleException ex,
                                   ExecutionResult result) {
    var commandName = result.getExecutionInfo().getCommandProperties().getMatchString();
    var params = Arrays.stream(result.getExecutionInfo()
            .getCommandProperties()
            .getCommandMethod()
            .getParameters())
        .filter((p) -> !p.getType().equals(ExecutionInfo.class))
        .toList();
    var ok = true;

    var embedBuilder = new EmbedBuilder()
        .setTitle("Command argument error")
        .setDescription("Command: " + commandName)
        .setTimestamp(TimeUtils.getDateTimeUTC());

    for (var param : params) {
      String desc = null;
      String title = "[" + param.getType().getSimpleName() + "] " + param.getName();

      if (!ok) {
        desc = DiscordUtils.EMO_RED_TRIANGLE + " BAD_ARGUMENT";
      } else if (param.equals(ex.getParameter())) {
        desc = DiscordUtils.EMO_RED_CIRCLE + " " + ex.getReason();
        ok = false;
      } else {
        desc = DiscordUtils.EMO_GREEN_SQUARE + " GOOD";
      }

      embedBuilder.addField(title, desc, false);
    }

    var embed = embedBuilder.build();

    var message = new MessageCreateBuilder()
        .setEmbeds(embed)
        .build();

    mre.getChannel()
        .sendMessage(message)
        .queue();
  }

  private void respondBPMException(MessageReceivedEvent mre, BotPermissionMismatchException bpme,
                                   ExecutionResult result) {
    var commandName = result.getExecutionInfo().getCommandProperties().getMatchString();
    var botPermission = result.getExecutionInfo().getCommandProperties().getBotPermissions();
    var missinPermission = bpme.getPermissionMissing();

    var embedBuilder = new EmbedBuilder()
        .setTitle("Bot permission mismatch")
        .setDescription("Command: " + commandName)
        .setTimestamp(TimeUtils.getDateTimeUTC());

    for (var perm : botPermission) {
      embedBuilder
          .addField(perm.getName(), missinPermission.contains(perm) ? "Missing" : "Acquired", false);
    }

    var embed = embedBuilder.build();

    var message = new MessageCreateBuilder()
        .setEmbeds(embed)
        .build();

    mre.getChannel()
        .sendMessage(message)
        .queue();
  }

  private void respondUPMException(MessageReceivedEvent mre, UserPermissionMismatchExecption bpme,
                                   ExecutionResult result) {
    var commandName = result.getExecutionInfo().getCommandProperties().getMatchString();
    var botPermission = result.getExecutionInfo().getCommandProperties().getBotPermissions();
    var missinPermission = bpme.getPermissionMissing();

    var embedBuilder = new EmbedBuilder()
        .setTitle("User permission mismatch")
        .setDescription("Command: " + commandName)
        .setTimestamp(TimeUtils.getDateTimeUTC());

    for (var perm : botPermission) {
      embedBuilder
          .addField(perm.getName(), missinPermission.contains(perm) ? "Missing" : "Acquired", false);
    }

    var embed = embedBuilder.build();

    var message = new MessageCreateBuilder()
        .setEmbeds(embed)
        .build();

    mre.getChannel()
        .sendMessage(message)
        .queue();
  }

  private void handleCommandSuccess(ExecutionResult executionResult) {
    var executionInfo = executionResult.getExecutionInfo();
    var event = (MessageReceivedEvent) executionResult.getExecutionInfo().getTriggerEvent();

    // Show log
    logger.info(
        "Executed command \"{}\" from guild \"{}\" requested by user \"{}\"",
        executionInfo.getCommandProperties().getMatchString(),
        event.getGuild().getName(),
        event.getAuthor().getAsTag());
  }
}
