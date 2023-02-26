package tk.vnvna.sodini.socket;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/event", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class SocketEntry {
  static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

  @OnOpen
  public void onOpen(Session session) {
    peers.add(session);
  }

  @OnMessage
  public void onMessage(SocketMessage message, Session session) throws IOException, EncodeException {
    switch (message.getEvent()) {
      case "TERMINATE" -> {
        System.exit(1);
      }
    }
  }

  @OnClose
  public void onClose(Session session) throws IOException, EncodeException {

  }
}
