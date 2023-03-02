package tk.vnvna.sodini.socket;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageDecoder implements Decoder.Text<SocketMessage> {

  private static Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

  @Override
  public SocketMessage decode(String s) throws DecodeException {
    try {
      return new ObjectMapper().readValue(s, SocketMessage.class);
    } catch (JsonProcessingException e) {
      logger.error("Cannot decode socket message due to error: " + e);
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
