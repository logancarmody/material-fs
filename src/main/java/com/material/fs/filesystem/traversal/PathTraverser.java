package com.material.fs.filesystem.traversal;

import com.material.fs.exceptions.BadPathException;
import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;


public class PathTraverser implements Traverser {
  Iterator<String> pathIterator;

  public PathTraverser(String path) {
    // TODO validate names
    this(path.split("/"));
  }

  public PathTraverser(String[] pathSegments) {
    // TODO validate names
    this.pathIterator = Arrays.stream(pathSegments).iterator();
  }

  public boolean shouldTerminate(File currentFile) {
    return !pathIterator.hasNext();
  }

  public File getNextFile(File currentFile) {
    if (!(currentFile instanceof Directory)) {
      throw new RuntimeException();
    }
    Directory directory = (Directory) currentFile;
    String pathSegment = pathIterator.next();
    return directory.findChild(pathSegment)
        .orElseGet(() -> handleNotFoundPathSegment(directory, pathSegment));
  }

  public File handleNotFoundPathSegment(Directory currentDirectory, String pathSegment) {
    throw new BadPathException(pathSegment);
  }

  public Optional<File> terminate(File currentFile) {
    return Optional.of(currentFile);
  }
}
