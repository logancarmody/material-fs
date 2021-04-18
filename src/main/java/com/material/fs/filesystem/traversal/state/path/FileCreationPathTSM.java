package com.material.fs.filesystem.traversal.state.path;

import com.material.fs.filesystem.exceptions.FileExistsException;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;
import java.util.Optional;


/**
 * This TSM traverses the path with the intention of creating a file at the end
 */
public class FileCreationPathTSM extends PathTSM {
  private boolean _createdFile = false;

  public FileCreationPathTSM(String path) {
    super(path);
  }

  @Override
  public File handleNotFoundPathSegment(Directory currentDirectory, String pathSegment) {
    if (!pathIterator.hasNext()) {
      _createdFile = true;
      return createResultingFile(currentDirectory, pathSegment);
    } else {
      return handleNotFoundDirectorySegment(currentDirectory, pathSegment);
    }
  }

  /**
   * This method handles if we encounter a directory segment of the path that doesn't exist
   */
  protected File handleNotFoundDirectorySegment(Directory currentDirectory, String pathSegment) {
    throw new RuntimeException();
  }

  /**
   * This method handles creating the resulting file at the end of the traversal
   */
  protected File createResultingFile(Directory currentDirectory, String fileName) {
    return currentDirectory.createEmptyContentFile(fileName);
  }

  @Override
  public Optional<File> terminate(File currentFile) {
    if (!_createdFile) {
      throw new FileExistsException(currentFile);
    }
    return super.terminate(currentFile);
  }
}
