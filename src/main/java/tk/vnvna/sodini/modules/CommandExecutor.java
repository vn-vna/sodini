package tk.vnvna.sodini.modules;

import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.controllers.annotations.ModuleEntry;
import tk.vnvna.sodini.controllers.helpers.AppService;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;
import tk.vnvna.sodini.discord.helpers.ExecutionResult;
import tk.vnvna.sodini.exceptions.BotPermissionMismatchException;
import tk.vnvna.sodini.exceptions.UserPermissionMismatchExecption;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
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

  @Getter
  private ThreadPoolExecutor threadPoolExecutor;

  @ModuleEntry
  public void initialize() {
    if (!jdaHandler.isEnabled())
    {
      logger.warn("Command executor is disabled since discord module is disabled");
      return;
    }

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
