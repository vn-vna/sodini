package tk.vnvna.sodini.modules;

import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.ModuleController;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.controllers.annotations.ModuleEntry;
import tk.vnvna.sodini.discord.annotations.BotPermission;
import tk.vnvna.sodini.discord.annotations.CommandGroup;
import tk.vnvna.sodini.discord.annotations.CommandMethod;
import tk.vnvna.sodini.discord.annotations.UserPermission;
import tk.vnvna.sodini.discord.helpers.CommandBase;
import tk.vnvna.sodini.discord.helpers.CommandProperties;
import tk.vnvna.sodini.utils.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AppModule
public class CommandLoader {
  @Dependency
  private Logger logger;

  @Dependency
  private ModuleController moduleController;

  @Dependency
  private Configuration configuration;

  @Dependency
  private JDAHandler jdaHandler;

  @Getter
  private HashMap<String, CommandProperties> commands;

  @Getter
  private Map<Class<? extends CommandBase>, CommandBase> commandGroups;

  @ModuleEntry
  public void loadCommands() {
    if (!jdaHandler.isEnabled()) {
      logger.warn("Command loader is disabled since discord module is disabled");
      return;
    }

    commands = new HashMap<>();
    commandGroups = new HashMap<>();

    loadGroups();
    loadMethods();
  }

  private void loadGroups() {

    moduleController.getModules().forEach((k, v) -> {
      var commandGroupAnnotation = k.getAnnotation(CommandGroup.class);

      if (Objects.isNull(commandGroupAnnotation)) {
        return;
      }

      if (!CommandBase.class.isAssignableFrom(k)) {
        return;
      }

      commandGroups.put((Class<? extends CommandBase>) k, (CommandBase) v);
      logger.debug("Detected command group {}", k.getSimpleName());
    });
  }

  private void loadMethods() {
    commandGroups.forEach((k, v) -> {
      Arrays.stream(k.getDeclaredMethods())
        .filter((m) -> Objects.nonNull(m.getAnnotation(CommandMethod.class)))
        .forEach((m) -> {
          // Get basic properties
          var commandProps = new CommandProperties();
          commandProps.setCommandGroup(v);
          commandProps.setCommandMethod(m);

          // Get command name
          var commandName = m.getAnnotation(CommandMethod.class).value();
          // Get match string
          var matchString = StringUtils.joinNonBlanks(" ", commandProps.getCommandGroup().getGroupMatcher(),
            commandName);
          commandProps.setMatchString(matchString);

          // Get command permissions
          var userPermissionAnnotation = m.getAnnotation(UserPermission.class);
          var userPermissions = Objects.isNull(userPermissionAnnotation) ? List.of() : List.of(userPermissionAnnotation.value());
          commandProps.setUserPermissions((List<Permission>) userPermissions);

          var botPermissionAnnotation = m.getAnnotation(BotPermission.class);
          var botPermissions = Objects.isNull(botPermissionAnnotation) ? List.of() : List.of(botPermissionAnnotation.value());
          commandProps.setBotPermissions((List<Permission>) botPermissions);

          commands.put(matchString, commandProps);
        });
    });
  }

  private void loadCommandTree() {

  }

}
