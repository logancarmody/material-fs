package com.material.fs.filesystem.exceptions;

/**
 * Exception for paths that don't exist
 */
public class BadPathException extends RuntimeException {
  public BadPathException(String path) {
    super(String.format("Path doesn't exist: %s", path));
  }
}
