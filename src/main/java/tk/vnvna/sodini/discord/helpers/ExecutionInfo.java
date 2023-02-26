package tk.vnvna.sodini.discord.helpers;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.Event;

import java.util.List;

@Getter
@Setter
public class ExecutionInfo {
  private CommandProperties commandProperties;
  private List<Object> commandArguments;
  private Event triggerEvent;
}
