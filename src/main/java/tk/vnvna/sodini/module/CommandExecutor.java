package tk.vnvna.sodini.module;

import lombok.Getter;
import tk.vnvna.sodini.controller.annotations.AppModule;
import tk.vnvna.sodini.controller.helper.AppService;
import tk.vnvna.sodini.discord.helper.ExecutionInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@AppModule
public class CommandExecutor implements AppService {

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

  public void executeCommand(ExecutionInfo executionInfo) {
    threadPoolExecutor.execute(() -> {
      var commandModule = executionInfo.getCommandProperties().getCommandGroup();
      var commandMethod = executionInfo.getCommandProperties().getCommandMethod();
      var paramList = commandMethod.getParameterTypes();
      var argList = executionInfo.getCommandArguments();

      try {
        if (paramList.length != 0 && paramList[0].equals(ExecutionInfo.class)) {
          argList.add(0, executionInfo);
        }

        commandMethod.invoke(commandModule, argList.toArray());
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
