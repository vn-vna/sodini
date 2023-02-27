package tk.vnvna.sodini.socket;

import jakarta.websocket.EncodeException;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerApplicationConfig;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import jakarta.websocket.server.ServerEndpointConfig.Builder;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SocketEntry implements ServerApplicationConfig {

  @Override
  public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
    var serverEndpointConfig = new HashSet<ServerEndpointConfig>();

    for (var endpoint : endpointClasses) {
//      serverEndpointConfig.add(ServerEndpointConfig.Builder.create(endpoint,))
    }

    return serverEndpointConfig;
  }

  @Override
  public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
    return null;
  }
}
