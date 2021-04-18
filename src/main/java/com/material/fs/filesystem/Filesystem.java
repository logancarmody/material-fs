package com.material.fs.filesystem;

import com.material.fs.editor.TextEditor;
import com.material.fs.exceptions.IllegalOperationException;
import com.material.fs.exceptions.InvalidFileNameException;
import com.material.fs.filesystem.traversal.DirectoryCreationPathTraverser;
import com.material.fs.filesystem.traversal.DirectoryCreationPathTraverserRecursive;
import com.material.fs.filesystem.traversal.FileCreationPathTraverserRecursive;
import com.material.fs.filesystem.traversal.FileCreationPathTraverser;
import com.material.fs.filesystem.traversal.FileSystemTraverser;
import com.material.fs.filesystem.traversal.PathTraverser;
import com.material.fs.filesystem.util.FilenameUtil;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;


/**
 * Methods to implement
 *
 * Create files
 *    create file in cwd
 *    create file at path
 *        relative
 *        absolute
 */
public class Filesystem {
  // VISIBLE FOR TESTING
  final Directory EOF;
  final Root _root;
  final FileSystemTraverser _traverser;
  final TextEditor _textEditor;

  public Filesystem() {
    _root = new Root();
    EOF = Root.getEof();
    _traverser = new FileSystemTraverser(EOF);
    _textEditor = new TextEditor();
  }

  public String cwd(File file) {
    // TODO handle orphaned file
    if (file == _root) {
      return _root.getName();
    }

    LinkedList<String> path = new LinkedList<>();
    path.add(file.getName());
    File parent = file.getParent();
    while (parent != EOF) {
      path.addFirst(parent.getName());
      parent = parent.getParent();
    }
    // TODO potentially memoize FQN
    return String.join("/", path);
  }

  public Collection<File> getDirsChildren(Directory cwd) {
    return cwd.getContents();
  }

  public Collection<File> getDirsChildren(Directory cwd, String path) {
    return getDirectoryAtPath(cwd, path).getContents();
  }

  private File getFileAtPath(Directory cwd, String path) {
    return _traverser.traverse(cwd, path, PathTraverser::new);
  }

  public ContentFile getContentFileAtPath(Directory cwd, String path) {
    return getFileAtPath(cwd, path).getContentFile();
  }

  public Directory getDirectoryAtPath(Directory cwd, String path) {
    return getFileAtPath(cwd, path).getDirectory();
  }

  public ContentFile createEmptyFile(Directory cwd, String path, boolean createAlongPath) {
    if (!FilenameUtil.isValidFilePath(path)) {
      throw new RuntimeException();
    }

    return  _traverser.traverse(cwd, path, createAlongPath
        ? FileCreationPathTraverserRecursive::new
        : FileCreationPathTraverser::new).getContentFile();
  }

  public ContentFile createFile(Directory cwd, String path, boolean createAlongPath, boolean openForEditing) {
    if (!FilenameUtil.isValidFilePath(path)) {
      throw new InvalidFileNameException(path);
    }

    ContentFile contentFile = _traverser.traverse(cwd, path, createAlongPath
        ? FileCreationPathTraverserRecursive::new
        : FileCreationPathTraverser::new).getContentFile();

    if (openForEditing) {
      _textEditor.renderFile(contentFile);
    }

    return contentFile;
  }

  public void editFile(Directory cwg, String path) {
    ContentFile contentFile = getContentFileAtPath(cwg, path);

    _textEditor.renderFile(contentFile);
  }

  public Directory createDirectory(Directory cwd, String path, boolean createAlongPath) {
    synchronized (_root) {
      return _traverser.traverse(cwd, path, createAlongPath
          ? DirectoryCreationPathTraverserRecursive::new
          : DirectoryCreationPathTraverser::new).getDirectory();
    }
  }

  public void moveFile(Directory cwd, String existingFilePath, String finalDirectoryPath, Optional<String> maybeNewName, boolean recursive) {
    moveFile(cwd, existingFilePath, finalDirectoryPath, maybeNewName, false, recursive);
  }

  public void copyFile(Directory cwd, String existingFilePath, String finalDirectoryPath, Optional<String> maybeNewName, boolean recursive) {
    moveFile(cwd, existingFilePath, finalDirectoryPath, maybeNewName, true, recursive);
  }

  private void moveFile(Directory cwd, String existingFilePath, String finalDirectoryPath, Optional<String> maybeNewName, boolean copy, boolean moveDirectories) {
    File existingFile = getFileAtPath(cwd, existingFilePath);

    if (!moveDirectories && existingFile instanceof Directory) {
      throw new IllegalOperationException(String.format("%s is a directory. If you would like to move it, use -r flag.", existingFile.getName()));
    }

    validateFile(cwd, existingFile);

    Directory destinationDirectory = getDirectoryAtPath(cwd, finalDirectoryPath);

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

  public String print(Directory cwd, String path) {
    return getFileAtPath(cwd, path).print();
  }

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

  public Directory getRootDirectory() {
    return _root;
  }
}
