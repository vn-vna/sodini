package tk.vnvna.sodini.utils;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {
  public static LocalDateTime getDateTimeUTC() {
    return LocalDateTime.now(Clock.system(ZoneId.of("UTC")));
  }
}
