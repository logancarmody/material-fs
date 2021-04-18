package com.material.fs.filesystem.traversal;

import com.material.fs.filesystem.File;
import java.util.Optional;


public interface Traverser {
  public boolean shouldTerminate(File currentFile);

  public File getNextFile(File currentFile);

  public Optional<File> terminate(File currentFile);
}
