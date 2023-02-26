package tk.vnvna.sodini.discord.helpers;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandTree {

  @Getter
  protected CommandNode rootNode;

  private CommandTree() {
    rootNode = new CommandNode(null);
  }

  public static CommandTree buildTree(Map<Class<? extends CommandBase>, CommandBase> commandMap) {
    var tree = new CommandTree();

    return tree;
  }

  @Getter
  public static class CommandNode {
    CommandProperties commandProperties;

    List<CommandNode> childNodes;

    public CommandNode(CommandProperties properties) {
      commandProperties = properties;
      childNodes = new ArrayList<>();
    }
  }
}
