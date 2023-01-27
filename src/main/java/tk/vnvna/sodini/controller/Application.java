package tk.vnvna.sodini.controller;

import lombok.Getter;
import tk.vnvna.sodini.controller.annotations.ModuleRoot;

import java.util.Objects;

public class Application {
  @Getter
  private final ModuleController moduleController;
  @Getter
  private final String moduleRoot;

  public Application() {
    var moduleRootAnnotation = this.getClass().getAnnotation(ModuleRoot.class);
    moduleRoot = Objects.nonNull(moduleRootAnnotation) ? moduleRootAnnotation.value() : "";
    moduleController = new ModuleController(moduleRoot);
  }

  public void start() {
    moduleController.startServices();
  }
}
