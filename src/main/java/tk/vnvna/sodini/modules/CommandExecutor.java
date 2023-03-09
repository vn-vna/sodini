package tk.vnvna.sodini.modules;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.controllers.annotations.ModuleEntry;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;
import tk.vnvna.sodini.discord.helpers.ExecutionResult;
import tk.vnvna.sodini.exceptions.ArgumentListNotCompatibleException;
import tk.vnvna.sodini.exceptions.BotPermissionMismatchException;
import tk.vnvna.sodini.exceptions.UserPermissionMismatchExecption;
import tk.vnvna.sodini.utils.DiscordUtils;
import tk.vnvna.sodini.utils.TimeUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@AppModule
public class CommandExecutor {

  @Dependency
  private ArgumentParser argumentParser;

  @Dependency
  private Logger logger;

  @Dependency
  private JDAHandler jdaHandler;

  @Dependency
  private Configuration configuration;

  @Dependency
  private CommandLoader commandLoader;

  @Dependency
  private CommandMatcher commandMatcher;


  @Getter
  private ThreadPoolExecutor threadPoolExecutor;

  @Getter
  private String commandPrefix;

  @ModuleEntry
  public void initialize() {
    if (!jdaHandler.isEnabled()) {
      logger.warn("Command executor is disabled since discord module is disabled");
      return;
    }

    commandPrefix = configuration.requireConfiguration("Discord::Prefix");

    int corePoolSize = 5;
    int maximumPoolSize = 10;
    long keepAliveTime = 500;
    TimeUnit unit = TimeUnit.SECONDS;

    BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>();
    RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();

    threadPoolExecutor = new ThreadPoolExecutor(
      corePoolSize,
      maximumPoolSize,
      keepAliveTime,
      unit,
      blockingQueue,
      rejectedExecutionHandler);

  }

  public void handleMessage(String message, Event triggerEvent) {
    if (message.startsWith(commandPrefix)) {
      var commandString = message.substring(commandPrefix.length());

      commandMatcher
        .matchCommand(triggerEvent, commandString)
        .ifPresent((executionInfo -> {
          try {
            var executionResult = this.executeCommand(executionInfo).get();

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

  private void respondANCException(MessageReceivedEvent mre,
                                   ArgumentListNotCompatibleException ex,
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

    resolveMissingPermissions(mre, botPermission, missinPermission, embedBuilder);
  }

  private void respondUPMException(MessageReceivedEvent mre, UserPermissionMismatchExecption bpme,
                                   ExecutionResult result) {
    var commandName = result.getExecutionInfo().getCommandProperties().getMatchString();
    var userPermission = result.getExecutionInfo().getCommandProperties().getUserPermissions();
    var missinPermission = bpme.getPermissionMissing();

    var embedBuilder = new EmbedBuilder()
      .setTitle("User permission mismatch")
      .setDescription("Command: " + commandName)
      .setTimestamp(TimeUtils.getDateTimeUTC());

    resolveMissingPermissions(mre, userPermission, missinPermission, embedBuilder);
  }

  private void resolveMissingPermissions(MessageReceivedEvent mre,
                                         List<Permission> userPermission,
                                         List<Permission> missinPermission,
                                         EmbedBuilder embedBuilder) {
    for (var perm : userPermission) {
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

  private void checkBotPermission(ExecutionInfo executionInfo) {
    List<Permission> permissionMissing = new ArrayList<>();

    var botUser = jdaHandler.getJda().getSelfUser();
    var botId = botUser.getId();
    var requiredPermissions = executionInfo.getCommandProperties().getBotPermissions();

    Member botAsMember = null;

    if (executionInfo.getTriggerEvent() instanceof MessageReceivedEvent mre) {
      botAsMember = mre.getGuild().getMemberById(botId);
    } else if (executionInfo.getTriggerEvent() instanceof SlashCommandInteractionEvent scie) {
      botAsMember = scie.getGuild().getMember(botUser);
    }

    for (var requiredPermission : requiredPermissions) {
      var acquired = PermissionUtil.checkPermission(botAsMember, requiredPermission);

      if (!acquired) {
        permissionMissing.add(requiredPermission);
      }
    }

    if (!permissionMissing.isEmpty()) {
      throw new BotPermissionMismatchException(botUser, executionInfo, permissionMissing);
    }
  }

  private void checkUserPermission(ExecutionInfo executionInfo) {
    List<Permission> permissionMissing = new ArrayList<>();

    User user = null;
    Member member = null;
    var requiredPermissions = executionInfo.getCommandProperties().getUserPermissions();

    if (executionInfo.getTriggerEvent() instanceof MessageReceivedEvent mre) {
      user = mre.getAuthor();
      member = mre.getMember();
    } else if (executionInfo.getTriggerEvent() instanceof SlashCommandInteractionEvent scie) {
      user = scie.getUser();
      member = scie.getMember();
    }

    for (var requiredPermission : requiredPermissions) {
      var acquired = PermissionUtil.checkPermission(member, requiredPermission);

      if (!acquired) {
        permissionMissing.add(requiredPermission);
      }
    }

    if (!permissionMissing.isEmpty()) {
      throw new UserPermissionMismatchExecption(user, executionInfo, permissionMissing);
    }
  }

  private void checkPreconditions(ExecutionInfo executionInfo) {
    checkBotPermission(executionInfo);
    checkUserPermission(executionInfo);
  }

  private void tryToExecute(ExecutionInfo executionInfo)
    throws InvocationTargetException, IllegalAccessException {
    checkPreconditions(executionInfo);

    var commandModule = executionInfo.getCommandProperties().getCommandGroup();
    var commandMethod = executionInfo.getCommandProperties().getCommandMethod();
    var paramList = List.of(commandMethod.getParameters());
    var argList = executionInfo.getCommandArguments();

    var args = argumentParser
      .convertArgumentList(executionInfo, argList, paramList)
      .toArray();

    commandMethod.invoke(commandModule, args);
  }

  public Future<ExecutionResult> executeCommand(ExecutionInfo executionInfo) {
    return threadPoolExecutor.submit(() -> {
      try {
        tryToExecute(executionInfo);
      } catch (Exception exception) {
        return new ExecutionResult(executionInfo, exception);
      }
      return new ExecutionResult(executionInfo);
    });
  }
}
