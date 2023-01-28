package tk.vnvna.sodini.discord.helper;

import lombok.Getter;
import tk.vnvna.sodini.app.EntryPoint;
import tk.vnvna.sodini.module.CommandLoader;

import java.util.Objects;
import java.util.Optional;

public class CommandMatcher {

  @Getter
  private final CommandLoader commandLoader;

  @Getter
  private final String commandString;

  public CommandMatcher(String commandString) {
    this.commandLoader = EntryPoint.getInstance()
        .getModuleController()
        .getModule(CommandLoader.class);

    this.commandString = commandString;
  }

  public Optional<ExecutionInfo> matchCommand() {
    ExecutionInfo executionInfo = null;

    for (var commandEntry : commandLoader.getCommands().entrySet()) {
      if (Objects.nonNull(executionInfo)) {
        break;
      }

      var cmdFound = commandString.indexOf(commandEntry.getKey());

      if (cmdFound != 0) {
        continue;
      }

      executionInfo = new ExecutionInfo();
      executionInfo.setCommandProperties(commandEntry.getValue());

      var argString = commandString.equals(commandEntry.getKey()) ?
          "" :
          commandString.substring(executionInfo.getCommandProperties().getMatchString().length() + 1);
      var argParser = new CommandArgumentParser(argString);
      var argArray = argParser.parse();

      executionInfo.setCommandArguments(argArray);
    }


    return Optional.ofNullable(executionInfo);
  }

}
