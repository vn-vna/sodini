package tk.vnvna.sodini.module;

import lombok.Getter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import tk.vnvna.sodini.controller.annotations.AppModule;
import tk.vnvna.sodini.controller.annotations.Dependency;
import tk.vnvna.sodini.controller.annotations.ModuleEntry;
import tk.vnvna.sodini.discord.annotations.CommandGroup;
import tk.vnvna.sodini.discord.annotations.CommandMethod;
import tk.vnvna.sodini.discord.helper.CommandBase;
import tk.vnvna.sodini.discord.helper.CommandMatcher;
import tk.vnvna.sodini.discord.helper.CommandProperties;
import tk.vnvna.sodini.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@AppModule
public class CommandLoader {
  public static final String COMMAND_PACKAGE = "tk.vnvna.sodini.discord.commands";

  @Getter
  private HashMap<String, CommandProperties> commands;

  @Getter
  private Map<Class<? extends CommandBase>, CommandBase> commandGroups;

  @Dependency
  private Logger logger;

  @ModuleEntry
  public void loadCommands() {
    commands = new HashMap<>();
    loadGroups();
    loadMethods();
  }

  private void loadGroups() {
    Reflections reflections = new Reflections(COMMAND_PACKAGE);

    commandGroups = reflections.getTypesAnnotatedWith(CommandGroup.class)
        .stream()
        .filter(CommandBase.class::isAssignableFrom)
        .collect(Collectors.toMap((c) -> (Class<? extends CommandBase>) c, (c) -> {
          try {
            var constructor = c.getDeclaredConstructor();
            return (CommandBase) constructor.newInstance();
          } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                   InvocationTargetException e) {
            throw new RuntimeException(e);
          }
        }));
  }

  private void loadMethods() {
    commandGroups.forEach((k, v) -> {
      Arrays.stream(k.getDeclaredMethods())
          .filter((m) -> Objects.nonNull(m.getAnnotation(CommandMethod.class)))
          .forEach((m) -> {
            var commandProps = new CommandProperties();
            commandProps.setCommandGroup(v);
            commandProps.setCommandMethod(m);

            var commandName = m.getAnnotation(CommandMethod.class).value();
            var matchString = StringUtils.joinNonBlanks(" ", commandProps.getCommandGroup().getGroupMatcher(), commandName);
            commandProps.setMatchString(matchString);

            commands.put(matchString, commandProps);
          });
    });
  }

}
