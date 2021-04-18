package com.material.fs.filesystem.traversal.state.search;

import com.material.fs.filesystem.models.File;
import com.material.fs.filesystem.traversal.state.TraversalStateManager;
import java.util.LinkedList;
import java.util.Queue;


/**
 * @inheritDoc
 *
 * Base class for a Search Traversal of the filesystem.
 * This class uses a BFS to traverse the filesystem.
 */
public abstract class SearchTSM<T> implements TraversalStateManager<T> {
  protected Queue<File> _filesToVisit;

  public SearchTSM() {
    _filesToVisit = new LinkedList<>();
  }

  @Override
  public File getNextFile(File currentFile) {
    if (currentFile.isDirectory()) {
      // Since the filesystem does not support symlinking, we don't have to check if we've visited a file before.
      _filesToVisit.addAll(currentFile.getDirectory().getContents());
    }
    return _filesToVisit.poll();
  }
}
