package tk.vnvna.sodini.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageEncoder implements Encoder.Text<SocketMessage> {
  public static final Logger logger = LoggerFactory.getLogger(MessageEncoder.class);

  @Override
  public String encode(SocketMessage message) throws EncodeException {
    try {
      return new ObjectMapper().writeValueAsString(message);
    } catch (JsonProcessingException e) {
      logger.error("Cannot encode message due to error: " + e);
    }

    return null;
  }

  @Override
  public void destroy() {

  }
}
