package com.material.fs.filesystem.exceptions;

public class InvalidFileNameException extends RuntimeException {
  public InvalidFileNameException(String filename) {
    super(String.format("%s is an invalid filename.", filename));
  }
}
