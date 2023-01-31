package tk.vnvna.sodini.modules;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import lombok.NonNull;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.exceptions.ConfigurationVariableNotFoundException;

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
    try (var fis = Configuration.class.getResourceAsStream(CFG_FILE)) {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();

      if (Objects.isNull(fis)) {
        return;
      }

      Document doc = documentBuilder.parse(fis);
      this.root = doc.getDocumentElement();
    } catch (ParserConfigurationException | IOException | SAXException e) {
      throw new RuntimeException(e);
    }
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

    Element crrElem = this.root;

    String[] paths = pattern.split("::");
    for (var dom : paths) {
      var nodes = crrElem.getElementsByTagName(dom);
      if (nodes.getLength() == 0) {
        return null;
      }

      if (nodes.getLength() > 1) {
        logger.warn("There are multiple node found for dom [{}], configuration would get only the first element", dom);
      }

      crrElem = (Element) nodes.item(0);
    }

    return Optional.ofNullable(crrElem.getTextContent());
  }

  public String requireConfiguration(String pattern) {
    var cfg = this.getConfiguration(pattern);

    if (cfg.isEmpty()) {
      throw new ConfigurationVariableNotFoundException();
    }

    return cfg.get();
  }

}
