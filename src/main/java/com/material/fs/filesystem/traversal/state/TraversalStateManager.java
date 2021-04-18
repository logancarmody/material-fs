package com.material.fs.filesystem.traversal.state;

import com.material.fs.filesystem.models.File;


/**
 * This is responsible for governing the traversal of our filesystem.
 *
 * This method is used by FileSystemTraverser#traverse method to navigate through the filesystem,
 * and then terminate at a certain point.
 * It can be implemented for a variety of traversal use-cases,
 * such as finding the file at a path, searching a sub-tree in the filesystem, etc.
 *
 * @param <R> the return type once this traverser terminates
 */
public interface TraversalStateManager<R> {

  /**
   * Given the currentFile, return true if we should stop traversing and terminate
   * @param currentFile the current file in the traversal
   */
  boolean shouldTerminate(File currentFile);

  /**
   * Given the current file, which file should the traverser visit next
   * @param currentFile the current file in the traversal
   * @return the next File to visit
   */
  File getNextFile(File currentFile);

  /**
   * Given the current file, terminate the traversal and return the requested Data.
   * @param currentFile the current file in the traversal
   * @return the result of the traversal
   */
  R terminate(File currentFile);
}
