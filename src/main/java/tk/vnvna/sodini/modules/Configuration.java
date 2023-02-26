package tk.vnvna.sodini.modules;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import lombok.NonNull;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.exceptions.ConfigurationVariableNotFoundException;
import tk.vnvna.sodini.utils.XmlUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
