package tk.vnvna.sodini.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tk.vnvna.sodini.modules.Configuration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

public class XmlUtils {

  public static Optional<String> getSingleValue(Element root, String pattern) {
    Element crrElem = root;

    String[] paths = pattern.split("::");
    for (var dom : paths) {
      var nodes = crrElem.getElementsByTagName(dom);
      if (nodes.getLength() == 0) {
        return Optional.empty();
      }

      crrElem = (Element) nodes.item(0);
    }

    return Optional.ofNullable(crrElem.getTextContent());
  }

  public static List<String> getAllValue(Element root, String pattern) {
    List<String> values = new ArrayList<>();
    Queue<Element> crrElems = new LinkedList<>();
    crrElems.add(root);

    String[] paths = pattern.split("::");

    for (var path : paths) {
      Queue<Element> nextElems = new LinkedList<>();

      while (!crrElems.isEmpty()) {
        var nodes = crrElems.poll().getElementsByTagName(path);

        for (int i = 0; i < nodes.getLength(); ++i) {
          nextElems.add((Element) nodes.item(i));
        }
      }

      crrElems = nextElems;
    }

    crrElems.forEach((element -> {
      values.add(element.getTextContent().trim());
    }));

    return values;
  }

  public static Element loadXmlResource(String path) {
    try (var fis = Configuration.class.getResourceAsStream(path)) {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();

      if (Objects.isNull(fis)) {
        return null;
      }

      Document doc = documentBuilder.parse(fis);
      return doc.getDocumentElement();
    } catch (ParserConfigurationException | IOException | SAXException e) {
      throw new RuntimeException(e);
    }
  }

}
