package tk.vnvna.sodini.modules.listeners;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class SlashCommandListener extends ListenerAdapter {
  @Override
  public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
    super.onSlashCommandInteraction(event);
  }

  @Override
  public void onGuildReady(@Nonnull GuildReadyEvent event) {
    super.onGuildReady(event);
  }
}
