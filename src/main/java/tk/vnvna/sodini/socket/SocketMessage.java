package tk.vnvna.sodini.socket;

import lombok.Data;

@Data
public class SocketMessage {
  private String event;
  private String timestamp;
  private String xmlData;
}
