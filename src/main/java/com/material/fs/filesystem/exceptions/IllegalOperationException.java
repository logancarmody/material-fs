package com.material.fs.filesystem.exceptions;

/**
 * Generic exception for illegal operations in the filesystem
 * TODO: figure out subclassing here
 */
public class IllegalOperationException extends RuntimeException {
  public IllegalOperationException(String message) {
    super(message);
  }
}
