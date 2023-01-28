package tk.vnvna.sodini.discord.helper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecutionInfo {
  private CommandProperties commandProperties;
  private Object[] commandArguments;
}
