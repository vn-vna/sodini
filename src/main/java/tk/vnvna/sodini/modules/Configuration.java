package tk.vnvna.sodini.modules;

import org.slf4j.Logger;
import org.w3c.dom.Element;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.exceptions.ConfigurationVariableNotFoundException;
import tk.vnvna.sodini.utils.XmlUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AppModule
public class Configuration {

  public static final String CFG_FILE = "/sodini.xml";

  @Dependency
  private Logger logger;

  private Element root = null;

  public void reload() {
    logger.info("Application requested to reload configuration from file {}", CFG_FILE);
    this.root = XmlUtils.loadXmlResource(CFG_FILE);
  }

  private void reloadGuard() {
    if (Objects.isNull(root)) {
      synchronized (Configuration.class) {
        if (Objects.isNull(root)) {
          reload();
        }
      }
    }
  }

  public List<String> getListConfiguration(String pattern) {
    reloadGuard();;
    return XmlUtils.getAllValue(this.root, pattern);
  }

  public Optional<String> getConfiguration(String pattern) {
    reloadGuard();
    return XmlUtils.getSingleValue(this.root, pattern);
  }

  public String requireConfiguration(String pattern) {
    var cfg = this.getConfiguration(pattern);

    if (cfg.isEmpty()) {
      throw new ConfigurationVariableNotFoundException();
    }

    return cfg.get();
  }

}
