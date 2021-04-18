package com.material.fs.filesystem.models;

import java.util.Optional;


public class Root extends Directory {
  private static final Directory EOF = new Directory();

  public Root() {
    super(EOF, "~");
    EOF.addFile(this);
  }

  public Optional<File> findChild(String fileName) {
    return super.findChild(fileName)
        .filter(file -> !EOF.equals(file));
  }

  public static Directory getEof() {
    return EOF;
  }
}
