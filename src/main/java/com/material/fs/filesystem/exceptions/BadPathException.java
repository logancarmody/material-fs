package com.material.fs.filesystem.exceptions;

public class BadPathException extends RuntimeException {
  public BadPathException(String path) {
    super(String.format("Path doesn't exist: %s", path));
  }
}
