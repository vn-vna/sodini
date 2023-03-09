package tk.vnvna.sodini.modules;

import net.dv8tion.jda.api.events.Event;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;

import java.util.Objects;
import java.util.Optional;

@AppModule
public class CommandMatcher {

  @Dependency
  private CommandLoader commandLoader;

  @Dependency
  private JDAHandler jdaHandler;

  @Dependency
  private ArgumentParser argumentParser;


  public Optional<ExecutionInfo> matchCommand(Event triggeredEvent, String commandString) {
    ExecutionInfo executionInfo = null;

    for (var commandEntry : commandLoader.getCommands().entrySet()) {
      if (Objects.nonNull(executionInfo)) {
        break;
      }

      var cmdFound = commandString.indexOf(commandEntry.getKey());

      if (cmdFound != 0) {
        continue;
      }

      var cmdExit = commandString.length() == commandEntry.getKey().length() ||
        commandString.charAt(commandEntry.getKey().length()) == ' ';

      if (!cmdExit) {
        continue;
      }

      executionInfo = new ExecutionInfo();
      executionInfo.setCommandProperties(commandEntry.getValue());

      var argString = commandString.equals(commandEntry.getKey()) ?
        "" :
        commandString.substring(executionInfo.getCommandProperties().getMatchString().length() + 1);
      var argArray = argumentParser.parse(argString);

      executionInfo.setCommandArguments(argArray);
      executionInfo.setTriggerEvent(triggeredEvent);
    }

    return Optional.ofNullable(executionInfo);
  }

}
