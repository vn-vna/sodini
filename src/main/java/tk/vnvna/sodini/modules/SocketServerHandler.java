package tk.vnvna.sodini.modules;

import jakarta.websocket.DeploymentException;
import lombok.Getter;
import org.glassfish.tyrus.server.Server;
import org.slf4j.Logger;
import tk.vnvna.sodini.controllers.annotations.AppModule;
import tk.vnvna.sodini.controllers.annotations.Dependency;
import tk.vnvna.sodini.controllers.annotations.ModuleEntry;
import tk.vnvna.sodini.socket.SocketServerConfiguration;

@AppModule
public class SocketServerHandler {

  @Dependency
  private Logger logger;

  @Dependency
  private CommandExecutor commandExecutor;

  @Dependency
  private Configuration configuration;

  @Getter
  private Server socketServer;

  @Getter
  private Thread socketServerThread;

  @Getter
  private boolean enabled;

  private int port;

  private String contextPath;

  private Object locker;

  @ModuleEntry
  public void initialize() {
    socketServerThread = new Thread(() -> {
      locker = new Object();

      var enabledString = configuration.requireConfiguration("Application::Socket::Enabled");
      enabled = Boolean.parseBoolean(enabledString);

      if (!enabled) {
        logger.warn("Canceled initialization of socket server since it is disabled");
        return;
      }

      port = Integer.parseInt(
          configuration
              .getConfiguration("Application::Socket::Port")
              .orElse("20402"));

      contextPath = configuration.getConfiguration("Application::Socket::ContextPath").orElse("/socket");

      socketServer = new Server("localhost", port, contextPath, null, SocketServerConfiguration.class);

      try {
        socketServer.start();
        logger.info("Socket server has successfully deployed on port {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
          logger.info("Stopping socket server");
          socketServer.stop();
        }));

        synchronized (locker) {
          locker.wait();
        }
      } catch (DeploymentException e) {
        logger.error(
            "Deploy socket server at port {} has been failed due to error: {}",
            port, e);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    socketServerThread.start();
  }
}
