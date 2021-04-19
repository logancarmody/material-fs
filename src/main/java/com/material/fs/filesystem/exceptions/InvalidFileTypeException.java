package com.material.fs.filesystem.exceptions;

/**
 * Exception to be thrown if the found file is not the proper type
 */
public class InvalidFileTypeException extends RuntimeException {
  public InvalidFileTypeException(String message) {
    super(message);
  }
}
