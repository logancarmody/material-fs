package com.material.fs.filesystem.exceptions;

import com.material.fs.filesystem.models.File;


public class FileExistsException extends RuntimeException {
  public FileExistsException(File file) {
    super(String.format("A file named %s already exists in this directory.", file.getName()));
  }
}
