package com.material.fs.filesystem.traversal.state.path;

import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;


/**
 * This is the same as {@link FileCreationPathTSM}, but it creates directories along the way.
 */
public class FileCreationPathTSMRecursive extends FileCreationPathTSM {
  public FileCreationPathTSMRecursive(String path) {
    super(path);
  }

  @Override
  public File handleNotFoundDirectorySegment(Directory currentDirectory, String pathSegment) {
    return currentDirectory.createDirectory(pathSegment);
  }
}
