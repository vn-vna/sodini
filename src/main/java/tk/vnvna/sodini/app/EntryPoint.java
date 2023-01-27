package tk.vnvna.sodini.app;

import tk.vnvna.sodini.controller.Application;
import tk.vnvna.sodini.controller.ModuleController;
import tk.vnvna.sodini.controller.annotations.ModuleRoot;

@ModuleRoot("tk.vnvna.sodini.module")
public class EntryPoint extends Application {
  public static void main(String[] args) {
    new EntryPoint().start();
  }
}
