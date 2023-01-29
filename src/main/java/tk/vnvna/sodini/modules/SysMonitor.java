package tk.vnvna.sodini.modules;

import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.controllers.helpers.AppService;

@AppModule
public class SysMonitor implements AppService {

  @Dependency
  private Logger logger;

  @Dependency
  private JDAHandler jdaHandler;

  @Override
  public void awake() {
    logger.info("Sys monitor has started");
  }
}
