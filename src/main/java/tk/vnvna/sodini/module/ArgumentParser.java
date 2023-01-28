package tk.vnvna.sodini.module;

import lombok.Getter;
import net.dv8tion.jda.api.entities.channel.Channel;
import tk.vnvna.sodini.controller.annotations.AppModule;
import tk.vnvna.sodini.controller.annotations.Dependency;
import tk.vnvna.sodini.controller.annotations.ModuleEntry;
import tk.vnvna.sodini.utils.Tokenizer;

import java.util.ArrayList;
import java.util.List;

@AppModule
public class ArgumentParser {

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
          var ts = token.getTokenString();
          var channelId = ts.substring(2, ts.length() - 1);
          var channel = jdaHandler.getJda().getChannelById(Channel.class, channelId);
          arguments.add(channel);
        }
        case USER_ID -> {
          var ts = token.getTokenString();
          var userId = ts.substring(2, ts.length() - 1);
          var user = jdaHandler.getJda().getUserById(userId);
          arguments.add(user);
        }
      }
    }

    return arguments;
  }

  enum TokenType {
    CHANNEL_ID,
    USER_ID,
    STRING,
    WORD
  }

}
