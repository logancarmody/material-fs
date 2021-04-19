package com.material.fs.filesystem.traversal.state.path;

import com.material.fs.filesystem.exceptions.IllegalOperationException;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;
import com.material.fs.filesystem.util.FileNameUtil;


/**
 * This TSM traverses the path with the intention of creating a file at the end
 */
public class DirectoryCreationPathTSM extends FileCreationPathTSM {
  public DirectoryCreationPathTSM(String path) {
    super(path);
  }

  @Override
  protected File createResultingFile(Directory currentDirectory, String fileName) {
    if (!FileNameUtil.isValidDirectoryPath(fileName)) {
      throw new IllegalOperationException("Cannot create a directory with an extension");
    }

    return currentDirectory.createDirectory(fileName);
  }
}
