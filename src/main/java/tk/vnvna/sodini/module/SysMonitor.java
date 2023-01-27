package tk.vnvna.sodini.module;

import org.slf4j.Logger;
import tk.vnvna.sodini.controller.annotations.AppModule;
import tk.vnvna.sodini.controller.annotations.Dependency;
import tk.vnvna.sodini.controller.helper.AppService;

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
