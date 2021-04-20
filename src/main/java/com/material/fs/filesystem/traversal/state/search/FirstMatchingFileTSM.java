package com.material.fs.filesystem.traversal.state.search;

import com.material.fs.filesystem.models.File;
import java.util.Optional;


/**
 * This TSM searches the filesystem for a filename matching a search string
 */
public class FirstMatchingFileTSM extends SearchTSM<Optional<File>> {
  private final String _searchString;

  public FirstMatchingFileTSM(String searchString) {
    super();
    _searchString = searchString;
  }

  @Override
  public boolean shouldTerminate(File currentFile) {
    // TODO change impl so if search string is prefix, we can do binary search
    if (currentFile.getName().contains(_searchString)) {
      return true;
    }
    // Check that we still have more tree to search
    return !currentFile.isDirectory() && _filesToVisit.isEmpty();
  }

  @Override
  public Optional<File> terminate(File currentFile) {
    return Optional.of(currentFile);
  }
}
