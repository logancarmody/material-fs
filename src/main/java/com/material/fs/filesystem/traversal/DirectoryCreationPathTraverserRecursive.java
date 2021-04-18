package com.material.fs.filesystem.traversal;

import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.File;


public class DirectoryCreationPathTraverserRecursive extends DirectoryCreationPathTraverser {
  public DirectoryCreationPathTraverserRecursive(String path) {
    super(path);
  }

  public DirectoryCreationPathTraverserRecursive(String[] path) {
    super(path);
  }

  @Override
  public File handleNotFoundDirectorySegment(Directory currentDirectory, String pathSegment) {
    return currentDirectory.createDirectory(pathSegment);
  }
}
