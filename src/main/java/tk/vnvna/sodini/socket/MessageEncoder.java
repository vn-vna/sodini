package tk.vnvna.sodini.socket;

import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;

import javax.json.Json;

public class MessageEncoder implements Encoder.Text<SocketMessage> {
  @Override
  public String encode(SocketMessage message) throws EncodeException {
    var encodedMessage = Json.createObjectBuilder()
        .add("event", message.getEvent())
        .add("timestamp", message.getTimestamp())
        .add("data", message.getXmlData())
        .build();
    return encodedMessage.toString();
  }

  @Override
  public void destroy() {

  }
}
