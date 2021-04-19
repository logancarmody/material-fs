package com.material.fs.filesystem.models;

import java.util.Optional;


/**
 * This is the root node in the file system
 */
public class Root extends Directory {
  private final Directory EOF;

  public Root() {
    super();
    EOF = new Directory();
    move(EOF);
    setName("~");
    EOF.addFile(this);
  }

  @Override
  public Optional<File> getChildFile(String fileName) {
    return super.getChildFile(fileName)
        .filter(file -> !EOF.equals(file));
  }

  /**
   * This method returns the EOF Directory, which is the root node's parent.
   */
  public Directory getEof() {
    return EOF;
  }
}
