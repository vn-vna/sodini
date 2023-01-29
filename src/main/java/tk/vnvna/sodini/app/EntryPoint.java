package tk.vnvna.sodini.app;

import tk.vnvna.sodini.controllers.Application;
import tk.vnvna.sodini.controllers.annotations.ModuleRoot;

import java.util.Objects;

@ModuleRoot("tk.vnvna.sodini.modules")
public class EntryPoint extends Application {
  private static EntryPoint instance = null;

  public static EntryPoint getInstance() {
    if (Objects.isNull(instance)) {
      instance = new EntryPoint();
    }
    return instance;
  }

  public static void main(String[] args) {
    getInstance().start();
  }
}
