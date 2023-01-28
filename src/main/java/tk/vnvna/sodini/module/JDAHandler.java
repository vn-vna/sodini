package tk.vnvna.sodini.module;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import tk.vnvna.sodini.controller.annotations.AppModule;
import tk.vnvna.sodini.controller.annotations.Dependency;
import tk.vnvna.sodini.controller.helper.AppService;

import java.util.Objects;

@AppModule
public class JDAHandler implements AppService {
  @Dependency
  private SysMonitor sysMonitor;

  @Dependency
  private Logger logger;

  @Dependency
  private Configuration configuration;

  @Getter
  private JDA jda;

  @Override
  public void awake() {
    var token = configuration.getConfiguration("Discord::Token");

    if (Objects.isNull(token) || token.isBlank()) {
      throw new IllegalArgumentException("Bot token is not found");
    }

    JDABuilder builder = JDABuilder.create(token, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));

    jda = builder.build();
  }
}
