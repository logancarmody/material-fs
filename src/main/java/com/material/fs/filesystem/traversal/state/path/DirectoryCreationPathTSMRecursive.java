package com.material.fs.filesystem.traversal.state.path;

import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;


/**
 * This is the same as {@link DirectoryCreationPathTSM}, but it creates directories along the way.
 */
public class DirectoryCreationPathTSMRecursive extends DirectoryCreationPathTSM {
  public DirectoryCreationPathTSMRecursive(String path) {
    super(path);
  }

  @Override
  public File handleNotFoundDirectorySegment(Directory currentDirectory, String pathSegment) {
    return currentDirectory.createDirectory(pathSegment);
  }
}
