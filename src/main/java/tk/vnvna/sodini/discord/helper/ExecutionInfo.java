package tk.vnvna.sodini.discord.helper;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.Event;

import java.util.List;

@Getter
@Setter
public class ExecutionInfo {
  private CommandProperties commandProperties;
  private List<Object> commandArguments;
  private Event triggerEvent;

}
