package com.material.fs.filesystem;

import com.material.fs.editor.TextEditor;
import com.material.fs.filesystem.exceptions.IllegalOperationException;
import com.material.fs.filesystem.exceptions.InvalidFileNameException;
import com.material.fs.filesystem.models.ContentFile;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;
import com.material.fs.filesystem.models.Root;
import com.material.fs.filesystem.traversal.state.path.DirectoryCreationPathTSM;
import com.material.fs.filesystem.traversal.state.path.DirectoryCreationPathTSMRecursive;
import com.material.fs.filesystem.traversal.state.path.FileCreationPathTSMRecursive;
import com.material.fs.filesystem.traversal.state.path.FileCreationPathTSM;
import com.material.fs.filesystem.traversal.FileSystemTraverser;
import com.material.fs.filesystem.traversal.state.path.PathTSM;
import com.material.fs.filesystem.util.FileNameUtil;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * The base filesystem class.
 * This class is stateless and just exposes different APIs for interacting with the filesystem.
 */
public class Filesystem {
  final Directory EOF;
  final Root _root;
  final FileSystemTraverser _traverser;
  final TextEditor _textEditor;

  public Filesystem() {
    _root = new Root();
    EOF = _root.getEof();
    _traverser = new FileSystemTraverser(EOF);
    _textEditor = new TextEditor();
  }

  /**
   * Returns the full string path given the current directory.
   *
   * @param cwd current working directory
   */
  public String currentFullPath(Directory cwd) {
    return getPath(EOF, cwd);
  }

  private String getPath(Directory stoppingPoint, File startingPoint) {
    if (startingPoint == _root) {
      return _root.getName();
    }

    LinkedList<String> path = new LinkedList<>();
    path.add(startingPoint.getName());
    File parent = startingPoint.getParent();
    while (parent != stoppingPoint) {
      if (parent == EOF) {
        throw new RuntimeException("Stopping point not in path");
      }
      path.addFirst(parent.getName());
      parent = parent.getParent();
    }

    // TODO potentially memoize FQN
    return String.join("/", path);
  }

  /**
   * Return a list of the directories child files
   */
  public Collection<File> getDirsChildren(Directory cwd) {
    return cwd.getContents();
  }

  /**
   * Return a list of a directory at the path's child files
   */
  public Collection<File> getDirsChildren(Directory cwd, String path) {
    return getDirectoryAtPath(cwd, path).getContents();
  }

  private File getFileAtPath(Directory cwd, String path) {
    return _traverser.traversePath(cwd, path, PathTSM::new);
  }

  ContentFile getContentFileAtPath(Directory cwd, String path) {
    return getFileAtPath(cwd, path).getContentFile();
  }

  /**
   * Get the directory at the given path
   * @param cwd current working directory
   * @param path path to directory
   */
  public Directory getDirectoryAtPath(Directory cwd, String path) {
    return getFileAtPath(cwd, path).getDirectory();
  }

  /**
   * Create a file at the given path
   * @param cwd current working directory
   * @param path the path to create the file at
   * @param createAlongPath if true, this command will create directories if they don't exist in the path
   * @param openForEditing if true, this command will open the file for editing
   * @return the created file
   */
  public ContentFile createFile(Directory cwd, String path, boolean createAlongPath, boolean openForEditing) {
    if (!FileNameUtil.isValidFilePath(path)) {
      throw new InvalidFileNameException(path);
    }

    ContentFile contentFile = _traverser.traversePath(cwd, path, createAlongPath
        ? FileCreationPathTSMRecursive::new
        : FileCreationPathTSM::new).getContentFile();

    if (openForEditing) {
      _textEditor.blockingRenderFile(contentFile);
    }

    return contentFile;
  }

  /**
   * Open a file for editing
   * @param cwg the current working directory
   * @param path the path to the file
   */
  public void editFile(Directory cwg, String path) {
    ContentFile contentFile = getContentFileAtPath(cwg, path);

    _textEditor.blockingRenderFile(contentFile);
  }

  /**
   * Create a new directory at the path
   * @param cwd the current working directory
   * @param path the path to create
   * @param createAlongPath if true, this command will create directories if they don't exist in the path
   * @return the created directory
   */
  public Directory createDirectory(Directory cwd, String path, boolean createAlongPath) {
    return _traverser.traversePath(cwd, path, createAlongPath
        ? DirectoryCreationPathTSMRecursive::new
        : DirectoryCreationPathTSM::new).getDirectory();
  }

  /**
   * Move an existing file to a new directory
   *
   * @param cwd current working directory
   * @param existingFilePath the path to the existing file
   * @param finalDirectoryPath the path to the destination directory
   * @param maybeNewName this is the new name for the file
   * @param recursive if true, this will also move directories
   */
  public void moveFile(Directory cwd, String existingFilePath, String finalDirectoryPath, Optional<String> maybeNewName, boolean recursive) {
    moveFile(cwd, existingFilePath, finalDirectoryPath, maybeNewName, false, recursive);
  }

  /**
   * Copy an existing file
   *
   * @param cwd current working directory
   * @param existingFilePath the path to the existing file
   * @param finalDirectoryPath the path to the destination directory
   * @param maybeNewName this is the new name for the file
   * @param recursive if true, this will also move directories
   */
  public void copyFile(Directory cwd, String existingFilePath, String finalDirectoryPath, Optional<String> maybeNewName, boolean recursive) {
    moveFile(cwd, existingFilePath, finalDirectoryPath, maybeNewName, true, recursive);
  }

  /**
   * Recursively search the CWG for a file which filename contains a string
   *
   * @param cwd current working directory
   * @param searchString the string to search the filenames for
   * @param findFirst if true, the search will stop after it finds it's first match
   * @return a string of the paths of the matches, otherwise {@link Optional#empty()}
   */
  public Optional<String> search(Directory cwd, String searchString, boolean findFirst) {
    if (findFirst) {
      return _traverser.searchPathForFile(cwd, searchString).map(file -> getPath(cwd, file));
    } else {
      return Optional.of(_traverser.searchPathForAllFiles(cwd, searchString))
          .filter(lst -> !lst.isEmpty())
          .map(lst -> lst.stream().map(file -> getPath(cwd, file)).collect(Collectors.joining("\n")));
    }
  }

  private void moveFile(Directory cwd, String existingFilePath, String finalDirectoryPath, Optional<String> maybeNewName, boolean copy, boolean moveDirectories) {
    File existingFile = getFileAtPath(cwd, existingFilePath);

    if (!moveDirectories && existingFile instanceof Directory) {
      throw new IllegalOperationException(String.format("%s is a directory. If you would like to move it, use -r flag.", existingFile.getName()));
    }

    validateFile(cwd, existingFile);

    Directory destinationDirectory = getDirectoryAtPath(cwd, finalDirectoryPath);

    maybeNewName.ifPresent(newName -> {
      if ((existingFile.isDirectory() && !FileNameUtil.isValidDirectoryPath(newName))
          || (existingFile.isContentFile() && !FileNameUtil.isValidFilePath(newName))) {
        throw new InvalidFileNameException(newName);
      }
    });

    String newFileName = maybeNewName.orElseGet(existingFile::getName);

    File existingFileCopy = existingFile.deepCopy();

    try {
      File file;
      if (copy) {
        file = existingFile.deepCopy();
      } else {
        file = existingFile;
        existingFile.getParent().deleteFile(existingFile);
      }

      file.setName(newFileName);
      destinationDirectory.addFile(file);
      file.move(destinationDirectory);
    } catch (Exception e) {
      // Restore state in the case of a failed move or copy
      if (!existingFileCopy.getParent().contains(existingFile)) {
        existingFileCopy.getParent().addFile(existingFileCopy);
      }

      throw e;
    }
  }

  /**
   * Print the current directory structure
   *
   * @param cwd current working directory
   * @param path path from where to print
   * @return the printed structure
   */
  public String print(Directory cwd, String path) {
    return getFileAtPath(cwd, path).print();
  }

  /**
   * Print the current directory structure
   *
   * @param cwd current working directory
   * @return the printed structure
   */
  public String print(Directory cwd) {
    return cwd.print();
  }

  /**
   * Delete an existing file
   *
   * @param cwd current working directory
   * @param path the path to the file to delete
   * @param deleteDirectory if true, this will delete a directory
   */
  public void deleteFile(Directory cwd, String path, boolean deleteDirectory) {
    File file = getFileAtPath(cwd, path);
    validateFile(cwd, file);
    if (file instanceof Directory && !deleteDirectory) {
      throw new IllegalOperationException("You are attempting to delete a directory. If you would like to delete a directory, add the -r flag.");
    }
    synchronized (_root) {
      file.getParent().deleteFile(file);
    }
  }

  private void validateFile(Directory cwd, File file) {
    if (file == _root || file == EOF) {
      throw new IllegalOperationException("You can't modify this file.");
    }
    if (isEqualToOrContains(file, cwd)) {
      throw new IllegalOperationException("You can't modify a parent directory");
    }
  }

  private boolean isEqualToOrContains(File parent, File child) {
    // TODO explore memoization techniques for optimization
    if (parent == child) {
      return true;
    }

    File cur = child;
    while (cur != _root) {
      cur = cur.getParent();
      if (cur == parent) {
        return true;
      }
    }

    return false;
  }

  /**
   * @return the filesystems root directory
   */
  public Directory getRootDirectory() {
    return _root;
  }
}
