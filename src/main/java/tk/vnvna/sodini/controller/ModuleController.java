package tk.vnvna.sodini.controller;

import lombok.Getter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.vnvna.sodini.controller.annotations.AppModule;
import tk.vnvna.sodini.controller.annotations.Dependency;
import tk.vnvna.sodini.controller.helper.AppService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModuleController {

  @Getter
  protected Map<Class<?>, Object> modules;
  @Getter
  protected Logger logger;

  public ModuleController(String pkg) {
    logger = LoggerFactory.getLogger(ModuleController.class);

    loadModule(pkg);
    autoInject();
  }

  private void loadModule(String pkg) {
    Reflections reflections = new Reflections(pkg);

    modules = reflections.getTypesAnnotatedWith(AppModule.class)
        .stream()
        .collect(Collectors.toMap((c) -> c, (c) -> {
          try {
            var constructor = c.getDeclaredConstructor();
            return constructor.newInstance();
          } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                   InvocationTargetException e) {
            throw new RuntimeException(e);
          }
        }));
    logger.info("Loaded {} module(s) from project", modules.size());
  }

  private void autoInject() {
    modules.forEach((k, v) -> {
      Arrays.stream(k.getDeclaredFields())
          .forEach((f) -> {

            var injectAnnotation = f.getAnnotation(Dependency.class);

            if (Objects.isNull(injectAnnotation)) {
              return;
            }

            if (f.getType().equals(Logger.class)) {
              forceSetValue(f, v, LoggerFactory.getLogger(k));
              logger.info("Appended logger to dependency {}", k.getSimpleName());
              return;
            }

            var module = this.getModule(f.getType());
            forceSetValue(f, v, module);

            logger.info(
                "Injected module instance {} to {} with field {}",
                module.getClass().getSimpleName(),
                k.getSimpleName(),
                f.getName());
          });
    });
  }

  private void forceSetValue(Field f, Object o, Object val) {
    try {
      f.setAccessible(true);
      f.set(o, val);
      f.setAccessible(f.canAccess(o));
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public void startServices() {
    this.modules.forEach((k, v) -> {
      if (v instanceof AppService service) {
        new Thread(service::awake, k.getSimpleName())
            .start();
      }
    });
  }

  public <T> T getModule(Class<T> clazz) {
    return (T) this.modules.get(clazz);
  }

}
