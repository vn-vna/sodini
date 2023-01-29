package tk.vnvna.sodini.modules;

import lombok.Getter;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.controllers.helpers.AppService;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;
import tk.vnvna.sodini.discord.helpers.ExecutionResult;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@AppModule
public class CommandExecutor implements AppService {


  @Dependency
  private ArgumentParser argumentParser;

  @Dependency
  private Logger logger;

  @Getter
  private ThreadPoolExecutor threadPoolExecutor;

  @Override
  public void awake() {
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

  private void tryToExecute(ExecutionInfo executionInfo) throws InvocationTargetException, IllegalAccessException {
    var commandModule = executionInfo.getCommandProperties().getCommandGroup();
    var commandMethod = executionInfo.getCommandProperties().getCommandMethod();
    var paramList = List.of(commandMethod.getParameters());
    var argList = executionInfo.getCommandArguments();

    if (paramList.size() != 0 && paramList.get(0).getType().equals(ExecutionInfo.class)) {
      argList.add(0, executionInfo);
    }

    commandMethod.invoke(commandModule, argumentParser.converArgumentList(argList, paramList).toArray());
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
