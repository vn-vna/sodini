package tk.vnvna.sodini.discord.helpers;

public interface ArgumentConverter<T> {
  T convert(String arg);
}
