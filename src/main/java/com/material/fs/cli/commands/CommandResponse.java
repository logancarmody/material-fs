package com.material.fs.cli.commands;

import com.material.fs.filesystem.Directory;


public class CommandResponse {
  private final String message;
  private final boolean isError;
  private final Directory cwg;

  public CommandResponse(String message, boolean isError, Directory cwg) {
    this.message = message;
    this.isError = isError;
    this.cwg = cwg;
  }

  public Directory getCwg() {
    return cwg;
  }

  // TODO convert this to printer
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    if (isError) {
      stringBuilder.append("ERROR: ");
    }
    stringBuilder.append(message);
    return stringBuilder.toString();
  }
}
