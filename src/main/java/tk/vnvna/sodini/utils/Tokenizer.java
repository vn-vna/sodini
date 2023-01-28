package tk.vnvna.sodini.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;

import javax.annotation.RegEx;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Tokenizer<E> {

  List<TokenInfo> tokenInfos;

  public Tokenizer() {
    tokenInfos = new ArrayList<>();
  }

  public void registerToken(@RegEx String regex, E tokenType) {
    var pattern = Pattern.compile("^" + regex);
    tokenInfos.add(new TokenInfo(pattern, tokenType));
  }

  public List<Token> tokenize(String str) {
    List<Token> tokens = new ArrayList<>();

    while (!str.isBlank()) {
      boolean match = false;

      for (var tokenInfo : tokenInfos) {
        var matcher = tokenInfo.getPattern().matcher(str);
        if (matcher.find()) {
          match = true;

          String tokenString = matcher.group().trim();
          tokens.add(new Token(tokenString, tokenInfo.getTokenType()));

          str = matcher.replaceFirst("").trim();

          break;
        }
      }

      if (!match) {
        var idx = str.indexOf(' ');
        var word = str.substring(0, idx);
        throw new InvalidTokenException("Tokenizer found an unexpected token near [" + word + "]");
      }
    }

    return tokens;
  }

  @Getter
  @AllArgsConstructor
  public class Token {
    private String tokenString;
    private E tokenType;
  }

  @Getter
  @AllArgsConstructor
  public class TokenInfo {
    private Pattern pattern;
    private E tokenType;
  }

}
