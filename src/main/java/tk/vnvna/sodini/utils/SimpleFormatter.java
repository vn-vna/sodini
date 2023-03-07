package tk.vnvna.sodini.utils;

import java.util.HashMap;
import java.util.Map;

public class SimpleFormatter {

  private String pattern;
  private String value;
  private boolean modified;
  private Map<String, String> entries;

  private SimpleFormatter(String pattern, Map<String, String> entries) {
    this.pattern = pattern;
    this.entries = entries;
    this.modified = true;
    this.value = null;
  }

  private void parse() {
    if (!modified) {
      return;
    }
    value = pattern;

    for (var entry : entries.entrySet()) {
      value = value.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue());
    }

    modified = false;
  }

  public SimpleFormatter entry(String key, Object value) {
    entries.put(key, value.toString());
    modified = true;
    return this;
  }

  public String getValue() {
    parse();

    return value;
  }

  public static SimpleFormatter create(String pattern, String... entries) {
    var entryMap = new HashMap<String, String>();

    for (var entry : entries) {
      var entryArr = entry.split("::");

      if (entryArr.length != 2) {
        throw new IllegalArgumentException("Illegal entry pattern passed into formatter");
      }

      entryMap.put(entryArr[0], entryArr[1]);
    }

    return new SimpleFormatter(pattern, entryMap);
  }

}
