package com.material.fs.filesystem.traversal.state.path;

import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;


/**
 * This TSM traverses the path with the intention of creating a file at the end
 */
public class DirectoryCreationPathTSM extends FileCreationPathTSM {
  public DirectoryCreationPathTSM(String path) {
    super(path);
  }

  @Override
  protected File createResultingFile(Directory currentDirectory, String fileName) {
    return currentDirectory.createDirectory(fileName);
  }
}
