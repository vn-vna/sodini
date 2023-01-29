package tk.vnvna.sodini.discord.helpers;

import lombok.Getter;
import net.dv8tion.jda.api.events.Event;
import tk.vnvna.sodini.modules.ArgumentParser;
import tk.vnvna.sodini.modules.CommandLoader;

import java.util.Objects;
import java.util.Optional;

public class CommandMatcher {

  @Getter
  private final CommandLoader commandLoader;

  @Getter
  private final String commandString;
  private final Event triggeredEvent;
  private ExecutionInfo executionInfo = null;
  private ArgumentParser argumentParser;

  public CommandMatcher(ArgumentParser argumentParser, CommandLoader commandLoader, String commandString, Event event) {
    this.commandLoader = commandLoader;
    this.triggeredEvent = event;
    this.commandString = commandString;
    this.argumentParser = argumentParser;
  }

  public Optional<ExecutionInfo> matchCommand() {

    for (var commandEntry : commandLoader.getCommands().entrySet()) {
      if (Objects.nonNull(executionInfo)) {
        break;
      }

      var cmdFound = commandString.indexOf(commandEntry.getKey());
      var cmdExit = commandString.length() == commandEntry.getKey().length() || commandString.charAt(commandEntry.getKey().length()) == ' ';

      if (cmdFound != 0 || !cmdExit) {
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
