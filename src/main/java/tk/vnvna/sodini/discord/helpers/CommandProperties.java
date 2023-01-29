package tk.vnvna.sodini.discord.helpers;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
public class CommandProperties {
  private Method commandMethod;
  private CommandBase commandGroup;
  private String matchString;
}
