package com.material.fs.filesystem.traversal.state.path;

import com.material.fs.filesystem.exceptions.BadPathException;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;
import com.material.fs.filesystem.traversal.state.TraversalStateManager;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;


/**
 * This is a basic TSM which traverses down a given String path in the filesystem
 */
public class PathTSM implements TraversalStateManager<Optional<File>> {
  Iterator<String> pathIterator;

  public PathTSM(String path) {
    this(path.split("/"));
  }

  public PathTSM(String[] pathSegments) {
    this.pathIterator = Arrays.stream(pathSegments).iterator();
  }

  @Override
  public boolean shouldTerminate(File currentFile) {
    return !pathIterator.hasNext();
  }

  @Override
  public File getNextFile(File currentFile) {
    if (!(currentFile instanceof Directory)) {
      throw new RuntimeException();
    }
    Directory directory = (Directory) currentFile;
    String pathSegment = pathIterator.next();
    return directory.getChildFile(pathSegment)
        .orElseGet(() -> handleNotFoundPathSegment(directory, pathSegment));
  }

  /**
   * This method determines what to do if it comes across a path segment which doesn't exist in the filesystem
   */
  protected File handleNotFoundPathSegment(Directory currentDirectory, String pathSegment) {
    throw new BadPathException(pathSegment);
  }

  @Override
  public Optional<File> terminate(File currentFile) {
    return Optional.of(currentFile);
  }
}
