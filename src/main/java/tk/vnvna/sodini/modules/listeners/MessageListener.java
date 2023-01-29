package tk.vnvna.sodini.modules.listeners;

import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.controllers.annotations.ModuleEntry;
import tk.vnvna.sodini.discord.helpers.CommandMatcher;
import tk.vnvna.sodini.modules.ArgumentParser;
import tk.vnvna.sodini.modules.CommandExecutor;
import tk.vnvna.sodini.modules.CommandLoader;
import tk.vnvna.sodini.modules.Configuration;

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
  private ArgumentParser commandArgumentParser;

  @Dependency
  private Logger logger;

  @Getter
  private String commandPrefix;

  @ModuleEntry
  public void initialize() {
    commandPrefix = configuration.getConfiguration("Discord::Prefix");
  }

  @Override
  public void onMessageReceived(@NotNull MessageReceivedEvent event) {
    super.onMessageReceived(event);

    var rawContent = event.getMessage().getContentRaw();
    if (rawContent.startsWith(commandPrefix)) {
      var commandString = rawContent.substring(2);
      var commandMatcher = new CommandMatcher(commandArgumentParser, commandLoader, commandString, event);


      commandMatcher.matchCommand()
          .ifPresent((executionInfo -> {
            try {
              var executionResult = commandExecutor.executeCommand(executionInfo).get();

              if (executionResult.isSuccess()) {
                logger.info(
                    "Executed command \"{}\" from guild \"{}\" requested by user \"{}\"",
                    executionInfo.getCommandProperties().getMatchString(),
                    event.getGuild().getName(),
                    event.getAuthor().getAsTag());
              } else {
                logger.error(
                    "Execution of command \"{}\" from guild \"{}\" requested by user \"{}\" has been cancelled due to error: {}",
                    executionInfo.getCommandProperties().getMatchString(),
                    event.getGuild().getName(),
                    event.getAuthor().getAsTag(),
                    executionResult.getException().getClass().getSimpleName()
                );
              }
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          }));
    }
  }
}
