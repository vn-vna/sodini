package tk.vnvna.sodini.discord.commands;

import tk.vnvna.sodini.discord.annotations.CommandGroup;
import tk.vnvna.sodini.discord.annotations.CommandMethod;
import tk.vnvna.sodini.discord.helper.CommandBase;

@CommandGroup({"public", "static", "void"})
public class PublicCommands extends CommandBase {

  @CommandMethod("ping")
  public void ping() {

  }
}
