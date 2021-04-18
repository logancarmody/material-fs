package com.material.fs.filesystem.traversal.state.search;

import com.material.fs.filesystem.models.File;
import java.util.LinkedList;
import java.util.List;


/**
 * This TSM searches the filesystem for all filenames matching a search string
 */
public class AllMatchingFilesTSM extends SearchTSM<List<File>> {
  private final String _searchString;
  private final List<File> _matches;

  public AllMatchingFilesTSM(String searchString) {
    super();
    _searchString = searchString;
    _matches = new LinkedList<>();
  }

  @Override
  public boolean shouldTerminate(File currentFile) {
    if (currentFile.isContentFile() && currentFile.getName().contains(_searchString)) {
      _matches.add(currentFile);
    }
    // Check that we still have more tree to search
    return !currentFile.isDirectory() && _filesToVisit.isEmpty();
  }

  @Override
  public List<File> terminate(File currentFile) {
    return _matches;
  }
}
