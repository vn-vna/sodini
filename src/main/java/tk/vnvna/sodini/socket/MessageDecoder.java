package tk.vnvna.sodini.socket;


import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonException;
import java.io.StringReader;

public class MessageDecoder implements Decoder.Text<SocketMessage> {

  private static Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

  @Override
  public SocketMessage decode(String s) throws DecodeException {
    try {
      var message = new SocketMessage();

      var jsonObject = Json.createReader(new StringReader(s))
          .readObject();
      message.setEvent(jsonObject.getString("event", SocketMessage.MSG_EMPTY));
      message.setTimestamp(jsonObject.getString("timestamp", null));
      message.setXmlData(jsonObject.getString("data", null));

      return message;
    } catch (JsonException ex) {
      logger.error("Message decode failed");
    }

    return null;
  }

  @Override
  public boolean willDecode(final String s) {
    return true;
  }

  @Override
  public void destroy() {

  }
}
