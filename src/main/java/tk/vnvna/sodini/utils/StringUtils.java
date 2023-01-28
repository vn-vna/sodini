package tk.vnvna.sodini.utils;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtils {
  public static String joinNonBlanks(String delim, String... strs) {
    return Stream.of(strs)
        .filter((s) -> Objects.nonNull(s) && !s.isBlank())
        .collect(Collectors.joining(delim));
  }
}
