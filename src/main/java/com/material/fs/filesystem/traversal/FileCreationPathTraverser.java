package com.material.fs.filesystem.traversal;

import com.material.fs.exceptions.FileExistsException;
import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.File;
import java.util.Optional;


public class FileCreationPathTraverser extends PathTraverser {
  private boolean _createdFile = false;

  public FileCreationPathTraverser(String path) {
    super(path);
  }

  public FileCreationPathTraverser(String[] path) {
    super(path);
  }

  @Override
  public File handleNotFoundPathSegment(Directory currentDirectory, String pathSegment) {
    if (!pathIterator.hasNext()) {
      _createdFile = true;
      return currentDirectory.createEmptyContentFile(pathSegment);
    } else {
      return handleNotFoundDirectorySegment(currentDirectory, pathSegment);
    }
  }

  public File handleNotFoundDirectorySegment(Directory currentDirectory, String pathSegment) {
    throw new RuntimeException();
  }

  public Optional<File> terminate(File currentFile) {
    if (!_createdFile) {
      throw new FileExistsException(currentFile);
    }
    return super.terminate(currentFile);
  }
}
