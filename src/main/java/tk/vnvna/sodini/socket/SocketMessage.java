package tk.vnvna.sodini.socket;

import lombok.Data;

@Data
public class SocketMessage {
  public static final String MSG_EMPTY = "MSG_EMPTY";
  public static final String MSG_TERMINATE = "MSG_TERMINATE";

  private String event;
  private String timestamp;
  private String xmlData;
}
