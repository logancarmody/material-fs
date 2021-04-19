package com.material.fs.filesystem.exceptions;

/**
 * Exception to be thrown if the file does not fit the proper structure
 */
public class InvalidFileNameException extends RuntimeException {
  public InvalidFileNameException(String filename) {
    super(String.format("%s is an invalid filename.", filename));
  }
}
