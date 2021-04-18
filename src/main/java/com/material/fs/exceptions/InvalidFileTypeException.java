package com.material.fs.exceptions;

import com.material.fs.filesystem.File;


public class InvalidFileTypeException extends RuntimeException {
  public InvalidFileTypeException(String message) {
    super(message);
  }
}
