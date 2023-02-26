package tk.vnvna.sodini.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tk.vnvna.sodini.discord.helpers.ExecutionInfo;

import java.lang.reflect.Parameter;

@Getter
@AllArgsConstructor
public class ArgumentListNotCompatibleException extends RuntimeException {
  private Parameter parameter;
  private ExecutionInfo executionInfo;
  private Reason reason;

  public enum Reason {
    NON_NULL,
    TYPE_ERROR
  }
}
