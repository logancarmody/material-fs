package com.material.fs.filesystem.traversal;

import com.material.fs.exceptions.BadPathException;
import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.File;
import com.material.fs.filesystem.util.FilenameUtil;
import java.util.Optional;
import java.util.function.Function;


public class FileSystemTraverser {
  private final Directory _eof;

  public FileSystemTraverser(Directory eof) {
    _eof = eof;
  }

  public <T extends PathTraverser> File traverse(Directory cwd, String filePath, Function<String, T> pathTraverserConstructor) {
    if (!FilenameUtil.isValidPath(filePath)) {
      throw new BadPathException(filePath);
    }
    Directory startDirectory = FilenameUtil.isAbsolutePath(filePath) ? _eof : cwd;
    return traverse(startDirectory, pathTraverserConstructor.apply(filePath))
        .orElseThrow(RuntimeException::new);
  }

  public Optional<File> traverse(File currentFile, Traverser traverser) {
    if (traverser.shouldTerminate(currentFile)) {
      return traverser.terminate(currentFile);
    }
    return traverse(traverser.getNextFile(currentFile), traverser);
  }
}
