package tk.vnvna.sodini.modules.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.modules.CommandExecutor;
import tk.vnvna.sodini.modules.Configuration;

import javax.annotation.Nonnull;

@AppModule
public class MessageListener extends ListenerAdapter {

  @Dependency
  private Configuration configuration;

  @Dependency
  private CommandExecutor commandExecutor;

  @Dependency
  private Logger logger;

  @Override
  public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
    super.onMessageReceived(event);
    var rawContent = event.getMessage().getContentRaw();
    commandExecutor.handleMessage(rawContent, event);
  }

}
