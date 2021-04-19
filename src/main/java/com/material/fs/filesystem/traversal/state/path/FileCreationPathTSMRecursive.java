package com.material.fs.filesystem.traversal.state.path;

import com.material.fs.filesystem.exceptions.IllegalOperationException;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;
import com.material.fs.filesystem.util.FileNameUtil;


/**
 * This is the same as {@link FileCreationPathTSM}, but it creates directories along the way.
 */
public class FileCreationPathTSMRecursive extends FileCreationPathTSM {
  public FileCreationPathTSMRecursive(String path) {
    super(path);
  }

  @Override
  public File handleNotFoundDirectorySegment(Directory currentDirectory, String pathSegment) {
    if (!FileNameUtil.isValidDirectoryPath(pathSegment)) {
      throw new IllegalOperationException("Cannot create a directory with an extension");
    }

    return currentDirectory.createDirectory(pathSegment);
  }
}
