package tk.vnvna.sodini.discord.helpers;

import lombok.Getter;

@Getter
public class ExecutionResult {
  ExecutionInfo executionInfo;
  Exception exception;
  boolean success;

  public ExecutionResult(ExecutionInfo executionInfo, Exception rte) {
    this.executionInfo = executionInfo;
    this.success = false;
    this.exception = rte;
  }

  public ExecutionResult(ExecutionInfo executionInfo) {
    this.executionInfo = executionInfo;
    this.success = true;
    this.exception = null;
  }
}
