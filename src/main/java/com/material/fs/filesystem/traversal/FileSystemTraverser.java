package com.material.fs.filesystem.traversal;

import com.material.fs.filesystem.exceptions.BadPathException;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;
import com.material.fs.filesystem.traversal.state.path.PathTSM;
import com.material.fs.filesystem.traversal.state.TraversalStateManager;
import com.material.fs.filesystem.traversal.state.search.AllMatchingFilesTSM;
import com.material.fs.filesystem.traversal.state.search.FirstMatchingFileTSM;
import com.material.fs.filesystem.util.FileNameUtil;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


/**
 * This class handles the traversal of the filesystem
 */
public class FileSystemTraverser {
  private final Directory _eof;

  public FileSystemTraverser(Directory eof) {
    _eof = eof;
  }

  /**
   * Using the given {@link PathTSM}, traverse the filesystem along the specified path
   *
   * @param cwd Current Working Directory
   * @param filePath the file path to walk
   * @param pathTraverserConstructor the constructor for the {@link PathTSM} to use
   * @param <T> the type of the {@link PathTSM} implementation
   * @return the resulting file
   */
  public <T extends PathTSM> File traversePath(Directory cwd, String filePath, Function<String, T> pathTraverserConstructor) {
    if (!FileNameUtil.isValidPath(filePath)) {
      throw new BadPathException(filePath);
    }
    Directory startDirectory = FileNameUtil.isAbsolutePath(filePath) ? _eof : cwd;
    return traverse(startDirectory, pathTraverserConstructor.apply(filePath))
        .orElseThrow(() -> new BadPathException(filePath));
  }

  /**
   * Search the subtree for the first filename which contain the search string
   *
   * @param startingPoint the starting point in the tree
   * @param searchString the search string
   * @return a File if found
   */
  public Optional<File> searchPathForFile(Directory startingPoint, String searchString) {
    return traverse(startingPoint, new FirstMatchingFileTSM(searchString));
  }

  /**
   * Search the subtree for the all filenames which contain the search string
   *
   * @param startingPoint the starting point in the tree
   * @param searchString the search string
   * @return a list of the matching files
   */
  public List<File> searchPathForAllFiles(Directory startingPoint, String searchString) {
    return traverse(startingPoint, new AllMatchingFilesTSM(searchString));
  }

  private <R> R traverse(File currentFile, TraversalStateManager<R> tsm) {
    if (tsm.shouldTerminate(currentFile)) {
      return tsm.terminate(currentFile);
    }
    return traverse(tsm.getNextFile(currentFile), tsm);
  }
}
