package com.material.fs.exceptions;

import com.material.fs.filesystem.File;


public class FileExistsException extends RuntimeException {
  public FileExistsException(File file) {
    super(String.format("A file named %s already exists in this directory.", file.getName()));
  }
}
