package tk.vnvna.sodini.modules;

import lombok.Getter;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.controllers.annotations.ModuleEntry;
import tk.vnvna.sodini.exceptions.ArgumentListNotCompatibleException;
import tk.vnvna.sodini.utils.Tokenizer;

import javax.annotation.Nonnull;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AppModule
public class ArgumentParser {

  @Dependency
  private Logger logger;

  @Dependency
  private JDAHandler jdaHandler;

  @Getter
  private Tokenizer<TokenType> tokenizer;

  @ModuleEntry
  public void initialize() {
    tokenizer = new Tokenizer<>();
    tokenizer.registerToken("<#\\w+>", TokenType.CHANNEL_ID);
    tokenizer.registerToken("<@\\w+>", TokenType.USER_ID);
    tokenizer.registerToken("\\\".+\\\"", TokenType.STRING);
    tokenizer.registerToken("[^\\s]+", TokenType.WORD);
  }

  public List<Object> parse(String argString) {
    List<Object> arguments = new ArrayList<>();

    var tokens = tokenizer.tokenize(argString);
    for (var token : tokens) {
      switch (token.getTokenType()) {
        case STRING, WORD -> {
          arguments.add(token.getTokenString());
        }
        case CHANNEL_ID -> {
          var channelId = getIdFromDiscordMention(token.getTokenString());
          var channel = jdaHandler.getJda()
              .getChannelById(Channel.class, channelId);
          arguments.add(channel);
        }
        case USER_ID -> {
          var userId = getIdFromDiscordMention(token.getTokenString());
          var user = jdaHandler.getJda().getUserById(userId);
          arguments.add(user);
        }
      }
    }

    return arguments;
  }

  public List<Object> converArgumentList(List<Object> arguments, List<Parameter> params) {
    if (arguments.size() != params.size()) {
      throw new ArgumentListNotCompatibleException();
    }

    int sz = arguments.size();

    for (int i = 0; i < sz; ++i) {
      arguments.set(i, convertArgument(arguments.get(i), params.get(i)));
    }

    return arguments;
  }

  /**
   * Pls don't care about this shit =)))
   * @param o
   * @param param
   * @return
   */
  private Object convertArgument(Object o, Parameter param) {
    if (Objects.isNull(o) || param.getType().equals(o.getClass())) {
      return o;
    }

    var aClass = param.getType();
    var nullable = Objects.isNull(param.getAnnotation(Nonnull.class));

    try {
      if (int.class.equals(aClass) || Integer.class.equals(aClass)) {
        if (o instanceof IMentionable u) {
          logger.warn("Conversion from mention id to integer may be failed");
          return Integer.parseInt(u.getId());
        }

        if (o instanceof String s) {
          return Integer.parseInt(s);
        }
      }

      if (long.class.equals(aClass) || Long.class.equals(aClass)) {
        if (o instanceof IMentionable u) {
          return Long.parseLong(u.getId());
        }

        if (o instanceof String s) {
          return Long.parseLong(s);
        }
      }

      if (float.class.equals(aClass) || Float.class.equals(aClass)) {
        if (o instanceof IMentionable) {
          throw new IllegalArgumentException("Cannot convert from mentionable object to float");
        }

        if (o instanceof String s) {
          return Float.parseFloat(s);
        }
      }

      if (double.class.equals(aClass) || Double.class.equals(aClass)) {
        if (o instanceof IMentionable) {
          throw new IllegalArgumentException("Cannot convert from mentionable object to double");
        }

        if (o instanceof String s) {
          return Double.parseDouble(s);
        }
      }

      if (String.class.equals(aClass)) {
        if (o instanceof IMentionable u) {
          return u.getId();
        }

        if (o instanceof String s) {
          return s;
        }
      }

      if (User.class.isAssignableFrom(aClass)) {
        if (o instanceof User u) {
          return u;
        }

        if (o instanceof Channel) {
          throw new IllegalArgumentException("Channel cannot be converted to User");
        }

        if (o instanceof String s) {
          var u = jdaHandler.getJda().getUserById(s);
          if (Objects.isNull(u)) {
            throw new IllegalArgumentException("Cannot get user with ID " + s);
          }
          return u;
        }
      }

      if (Channel.class.isAssignableFrom(aClass)) {
        if (o instanceof User) {
          throw new IllegalArgumentException("User cannot be converted to Channel");
        }

        if (o instanceof Channel c) {
          return c;
        }

        if (o instanceof String s) {
          var u = jdaHandler.getJda().getChannelById(Channel.class, s);
          if (Objects.isNull(u)) {
            throw new IllegalArgumentException("Cannot get channel with ID " + s);
          }
          return u;
        }
      }

      throw new IllegalArgumentException("An unexpected argument found with type: " + o.getClass().getName());
    } catch (Exception ex) {
      if (nullable) {
        return null;
      }

      throw new RuntimeException(ex);
    }
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
