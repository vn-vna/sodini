package tk.vnvna.sodini.modules;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.ModuleController;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.controllers.helpers.AppService;

import java.util.Objects;

@AppModule
public class JDAHandler implements AppService {

  @Dependency
  private Logger logger;

  @Dependency
  private Configuration configuration;

  @Dependency
  private ModuleController moduleController;

  @Getter
  private JDA jda;

  private void buildSlashCommand(JDABuilder builder) {

  }

  private void loadListeners(JDABuilder builder) {
    moduleController.getModules().forEach((k, v) -> {
      if (ListenerAdapter.class.isAssignableFrom(k)) {
        builder.addEventListeners(v);
        logger.debug("Detected and assigned listener {} to JDA", k.getSimpleName());
      }
    });
  }

  @Override
  public void awake() {
    var enabled = Boolean.parseBoolean(configuration.requireConfiguration("Discord::Enabled"));
    if (!enabled)
    {
      logger.warn("Cancelled initialization of discord module since it is disabled");
      return;
    }

    var token = configuration.requireConfiguration("Discord::Token");

    if (Objects.isNull(token) || token.isBlank()) {
      throw new IllegalArgumentException("Bot token is not found");
    }

    JDABuilder builder = JDABuilder.create(
        token,
        GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));

    buildSlashCommand(builder);
    loadListeners(builder);

    jda = builder.build();
  }

  public boolean isEnabled() {
    return Boolean.parseBoolean(configuration.requireConfiguration("Discord::Enabled"));
  }
}
