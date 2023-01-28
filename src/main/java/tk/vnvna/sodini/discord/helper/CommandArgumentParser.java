package tk.vnvna.sodini.discord.helper;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import tk.vnvna.sodini.app.EntryPoint;
import tk.vnvna.sodini.module.JDAHandler;
import tk.vnvna.sodini.utils.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandArgumentParser {

  private static Tokenizer<TokenType> tokenizer;
  private final String argString;
  private final List<Object> arguments;

  protected CommandArgumentParser(String argString) {
    this.argString = argString;
    this.arguments = new ArrayList<>();
  }

  public static Tokenizer<TokenType> getTokenizer() {
    if (Objects.isNull(tokenizer)) {
      synchronized (CommandArgumentParser.class) {
        if (Objects.isNull(tokenizer)) {
          tokenizer = new Tokenizer<>();
          tokenizer.registerToken("<#\\w+>", TokenType.CHANNEL_ID);
          tokenizer.registerToken("<@\\w+>", TokenType.USER_ID);
          tokenizer.registerToken("\\\".+\\\"", TokenType.STRING);
          tokenizer.registerToken("[^\\s]+", TokenType.WORD);
        }
      }
    }

    return tokenizer;
  }

  private JDA getJda() {
    return EntryPoint.getInstance()
        .getModuleController()
        .getModule(JDAHandler.class)
        .getJda();
  }

  public Object[] parse() {
    arguments.clear();

    var tk = CommandArgumentParser.getTokenizer();
    var tokens = tk.tokenize(argString);
    for (var token : tokens) {
      switch (token.getTokenType()) {
        case STRING, WORD -> {
          arguments.add(token.getTokenString());
        }
        case CHANNEL_ID -> {
          var ts = token.getTokenString();
          var channelId = ts.substring(2, ts.length() - 3);
          var channel = getJda().getChannelById(Channel.class, channelId);
          arguments.add(channel);
        }
        case USER_ID -> {
          var ts = token.getTokenString();
          var userId = ts.substring(3, ts.length() - 4);
          var user = getJda().getUserById(userId);
          arguments.add(user);
        }
      }
    }

    return arguments.toArray();
  }

  enum TokenType {
    CHANNEL_ID,
    USER_ID,
    STRING,
    WORD
  }

}
