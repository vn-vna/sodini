package tk.vnvna.sodini.socket;

import jakarta.websocket.Endpoint;
import jakarta.websocket.server.ServerApplicationConfig;
import jakarta.websocket.server.ServerEndpointConfig;

import java.util.HashSet;
import java.util.Set;

public class SocketServerConfiguration implements ServerApplicationConfig {

  @Override
  public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
    var serverEndpointConfig = new HashSet<ServerEndpointConfig>();

    for (var endpoint : endpointClasses) {
      serverEndpointConfig.add(
          ServerEndpointConfig.Builder
              .create(endpoint, "/event")
              .build());
    }

    return serverEndpointConfig;
  }

  @Override
  public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
    return null;
  }
}
