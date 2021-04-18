package com.material.fs.exceptions;

import com.material.fs.filesystem.File;


public class InvalidFileNameException extends RuntimeException {
  public InvalidFileNameException(String filename) {
    super(String.format("%s is an invalid filename.", filename));
  }
}
