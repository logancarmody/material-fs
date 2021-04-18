package com.material.fs.filesystem.traversal;

import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.File;


public class FileCreationPathTraverserRecursive extends FileCreationPathTraverser{
  public FileCreationPathTraverserRecursive(String path) {
    super(path);
  }

  public FileCreationPathTraverserRecursive(String[] path) {
    super(path);
  }

  @Override
  public File handleNotFoundDirectorySegment(Directory currentDirectory, String pathSegment) {
    return currentDirectory.createDirectory(pathSegment);
  }
}
