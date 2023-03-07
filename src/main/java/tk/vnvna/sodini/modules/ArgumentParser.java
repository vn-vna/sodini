package tk.vnvna.sodini.modules;

import lombok.Getter;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.controllers.annotations.ModuleEntry;
import tk.vnvna.sodini.discord.helpers.ArgumentConverter;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;
import tk.vnvna.sodini.exceptions.ArgumentListNotCompatibleException;
import tk.vnvna.sodini.exceptions.ArgumentListNotCompatibleException.Reason;
import tk.vnvna.sodini.utils.Tokenizer;

import javax.annotation.Nonnull;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AppModule
public class ArgumentParser {

  @Dependency
  private Logger logger;

  @Dependency
  private JDAHandler jdaHandler;

  @Dependency
  private Configuration configuration;

  @Getter
  private Tokenizer<TokenType> tokenizer;

  @Getter
  private Map<Class<?>, ArgumentConverter<?>> converters;

  @ModuleEntry
  public void initialize() {
    if (!jdaHandler.isEnabled()) {
      logger.warn("Argument parser is disabled since discord module is disabled");
      return;
    }

    initTokenizer();
    initConverters();
  }

  private void initTokenizer() {
    tokenizer = new Tokenizer<>();

    tokenizer.registerToken("<#\\w+>", TokenType.CHANNEL_ID);
    tokenizer.registerToken("<@\\w+>", TokenType.USER_ID);
    tokenizer.registerToken("\\\".+\\\"", TokenType.STRING);
    tokenizer.registerToken("[^\\s]+", TokenType.WORD);
  }

  private void initConverters() {
    converters = new Hashtable<>();

    assignConverter(Integer.class, Integer::parseInt);
    assignConverter(int.class, Integer::parseInt);

    assignConverter(Long.class, Long::parseLong);
    assignConverter(long.class, Long::parseLong);

    assignConverter(Float.class, Float::parseFloat);
    assignConverter(float.class, Float::parseFloat);

    assignConverter(Double.class, Double::parseDouble);
    assignConverter(double.class, Double::parseDouble);

    assignConverter(User.class, (s) -> jdaHandler.getJda().getUserById(s));

    assignConverter(Channel.class, (s) -> jdaHandler.getJda().getChannelById(Channel.class, s));
    assignConverter(TextChannel.class, (s) -> jdaHandler.getJda().getChannelById(TextChannel.class, s));
    assignConverter(VoiceChannel.class, (s) -> jdaHandler.getJda().getChannelById(VoiceChannel.class, s));
    assignConverter(ForumChannel.class, (s) -> jdaHandler.getJda().getChannelById(ForumChannel.class, s));
    assignConverter(PrivateChannel.class, (s) -> jdaHandler.getJda().getChannelById(PrivateChannel.class, s));
    assignConverter(GuildChannel.class, (s) -> jdaHandler.getJda().getChannelById(GuildChannel.class, s));
    assignConverter(NewsChannel.class, (s) -> jdaHandler.getJda().getChannelById(NewsChannel.class, s));
    assignConverter(StageChannel.class, (s) -> jdaHandler.getJda().getChannelById(StageChannel.class, s));
    assignConverter(ThreadChannel.class, (s) -> jdaHandler.getJda().getChannelById(ThreadChannel.class, s));
  }

  public List<Object> parse(String argString) {
    List<Object> arguments = new ArrayList<>();

    var tokens = tokenizer.tokenize(argString);
    for (var token : tokens) {
      switch (token.getTokenType()) {
        case STRING, WORD -> {
          arguments.add(token.getTokenString());
        }
        case CHANNEL_ID, USER_ID -> {
          var channelId = getIdFromDiscordMention(token.getTokenString());
          arguments.add(channelId);
        }
      }
    }

    return arguments;
  }

  public List<Object> convertArgumentList(ExecutionInfo executionInfo, List<Object> arguments, List<Parameter> params) {
    List<Object> convertedArguments = new ArrayList<>();
    int i = 0;
    int j = 0;

    while (i < params.size()) {

      var param = params.get(i);
      var nullable = Objects.isNull(param.getAnnotation(Nonnull.class));

      if (param.getType().equals(ExecutionInfo.class)) {
        convertedArguments.add(executionInfo);
        ++i;
        continue;
      }

      // Convert
      Object arg;
      if (j >= arguments.size()) {
        arg = null;
      } else {
        arg = convertArgument(executionInfo, arguments.get(j), param);
      }

      if (Objects.isNull(arg) && !nullable) {
        throw new ArgumentListNotCompatibleException(
          param,
          executionInfo,
          Reason.NON_NULL);
      }

      convertedArguments.add(arg);

      ++i;
      ++j;
    }

    return convertedArguments;
  }

  private Object convertArgument(ExecutionInfo executionInfo, Object o, Parameter param) {
    if (Objects.isNull(o) || param.getType().equals(o.getClass())) {
      return o;
    }
    var nullable = Objects.isNull(param.getAnnotation(Nonnull.class));

    try {
      if (o instanceof String s && converters.containsKey(param.getType())) {
        return converters.get(param.getType()).convert(s);
      }

      throw new ArgumentListNotCompatibleException(
        param,
        executionInfo,
        Reason.TYPE_ERROR);

    } catch (Exception ex) {
      if (nullable) {
        return null;
      }

      throw ex;
    }
  }

  private <T> void assignConverter(Class<T> type, ArgumentConverter<T> method) {
    converters.put(type, method);
  }

  public String getIdFromDiscordMention(String mention) {
    return mention.substring(2, mention.length() - 1);
  }

  enum TokenType {
    CHANNEL_ID,
    USER_ID,
    STRING,
    WORD
  }

}
