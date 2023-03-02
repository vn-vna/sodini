package tk.vnvna.sodini.modules.endpoints;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.socket.SocketEntryBase;

@AppModule
public class EventSocketEndpoint extends SocketEntryBase {
  @Override
  public void onOpen(Session session, EndpointConfig config) {

  }
}
