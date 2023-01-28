package tk.vnvna.sodini.module.listeners;

import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tk.vnvna.sodini.controller.annotations.AppModule;
import tk.vnvna.sodini.controller.annotations.Dependency;
import tk.vnvna.sodini.controller.annotations.ModuleEntry;
import tk.vnvna.sodini.discord.helper.CommandMatcher;
import tk.vnvna.sodini.module.CommandExecutor;
import tk.vnvna.sodini.module.Configuration;

@AppModule
public class MessageListener extends ListenerAdapter {

  @Dependency
  private Configuration configuration;

  @Dependency
  private CommandExecutor commandExecutor;

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
      var commandMatcher = new CommandMatcher(commandString);
      commandMatcher.matchCommand()
          .ifPresent((executionInfo -> {
            commandExecutor.executeCommand(executionInfo);
          }));
    }
  }
}
