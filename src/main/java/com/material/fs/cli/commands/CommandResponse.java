package com.material.fs.cli.commands;

import com.material.fs.filesystem.models.Directory;


/**
 * This is the model returned by {@link Command}s.
 * It contains the message, whether the response was an error, and the new CWG
 */
public class CommandResponse {
  private final String message;
  private final boolean isError;
  private final Directory cwg;

  public CommandResponse(String message, boolean isError, Directory cwg) {
    this.message = message;
    this.isError = isError;
    this.cwg = cwg;
  }

  /**
   * @return the Current Working Directory which is the result from the Command.
   */
  public Directory getCwg() {
    return cwg;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    if (isError) {
      stringBuilder.append("ERROR: ");
    }
    stringBuilder.append(message);
    return stringBuilder.toString();
  }
}
