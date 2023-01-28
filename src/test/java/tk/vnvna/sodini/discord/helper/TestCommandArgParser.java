package tk.vnvna.sodini.discord.helper;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TestCommandArgParser {

  @Test
  public void Test() {
    var cap = new CommandArgumentParser("a-simple-string <#iadsidjaoisd>");
    var t = cap.parse();
    System.out.println(Arrays.toString(t));
  }

}
