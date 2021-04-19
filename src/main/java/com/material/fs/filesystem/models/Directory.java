package com.material.fs.filesystem.models;

import com.material.fs.filesystem.exceptions.BadPathException;
import com.material.fs.filesystem.exceptions.FileExistsException;
import com.material.fs.filesystem.util.Constants;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * A file which contains other files
 */
public class Directory extends File {
  protected Map<String, File> _contents;

  public Directory() {
    this(null, null, new TreeMap<>());
  }

  Directory(Directory parent, String name) {
    this(parent, name, new TreeMap<>());
  }

  Directory(Directory parent, String name, Map<String, File> contents) {
    super(parent, name);
    _contents = contents;
  }

  @Override
  public File deepCopy() {
    Map<String, File> newContents = _contents.values().stream()
        .map(File::deepCopy)
        .collect(Collectors.toMap(File::getName, Function.identity(), (left, right) -> left, TreeMap::new));

    return new Directory(_parent, _name, newContents);
  }

  /**
   * @return the contents of this directory
   */
  public Collection<File> getContents() {
    return _contents.values();
  }

  /**
   * Create an empty content file inside this directory
   *
   * @param filename the name for the file
   * @return the created file
   */
  public ContentFile createEmptyContentFile(String filename) {
    if (getChildFile(filename).isPresent()) {
      throw new RuntimeException();
    }

    ContentFile file = new ContentFile(this, filename);
    _contents.put(file.getName(), file);
    return file;
  }

  /**
   * Checks if the given file is in this directory.
   * It does not check recursively
   */
  public boolean contains(File file) {
    return file.equals(_contents.get(file.getName()));
  }

  /**
   * Create a directory inside this directory
   *
   * @param filename the name for the directory
   * @return the new Directory
   */
  public Directory createDirectory(String filename) {
    if (getChildFile(filename).isPresent()) {
      throw new FileExistsException(getChildFile(filename).get());
    }

    Directory directory = new Directory(this, filename);
    _contents.put(directory.getName(), directory);
    return directory;
  }

  /**
   * Returns either a child file, it's parent or itself depending on the filename.
   * This method can be used for traversing through this directory.
   *
   * @param fileName filename. This can be "." or ".."
   * @return the file.
   */
  public Optional<File> getChildFile(String fileName) {
    if (fileName.equals(Constants.CURRENT_DIRECTORY_SHORTCUT)) {
      return Optional.of(this);
    }

    if (fileName.equals(Constants.PARENT_DIRECTORY_SHORTCUT)) {
      return Optional.ofNullable(getParent());
    }

    return Optional.ofNullable(_contents.get(fileName));
  }


  // Just for testing
  public Directory getChildDirectory(String fileName) {
    if (fileName.equals(Constants.CURRENT_DIRECTORY_SHORTCUT)) {
      return this;
    }

    if (fileName.equals(Constants.PARENT_DIRECTORY_SHORTCUT)) {
      return getParent();
    }

    return _contents.get(fileName).getDirectory();
  }

  // For testing
  public Map<String, File> getContentMap() {
    return _contents;
  }

  /**
   * Delete the given file from this directory
   *
   * @param file the file to delete
   */
  public void deleteFile(File file) {
    File removedFile = _contents.remove(file.getName());
    if (removedFile == null) {
      throw new BadPathException(file.getName());
    }
    removedFile._parent = null;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Directory)) {
      return false;
    }

    Directory other = (Directory) o;

    return Objects.equals(this._parent, other._parent)
        && Objects.equals(this._name, other._name)
        && Objects.equals(this._contents, other._contents);
  }

  /**
   * Add a file to this directory
   *
   * @param file the file to add
   */
  public void addFile(File file) {
    if (!_contents.containsKey(file.getName())) {
      _contents.put(file.getName(), file);
      return;
    }
    throw new FileExistsException(file);
  }

  @Override
  protected void print(StringBuilder builder, String prefix, String childrenPrefix) {
    builder.append(prefix)
        .append(_name)
        .append("\n");

    Iterator<File> it = _contents.values().iterator();
    while (it.hasNext()) {
      File next = it.next();
      if (it.hasNext()) {
        next.print(builder, childrenPrefix + "|-- ", childrenPrefix + "│   ");
      } else {
        next.print(builder, childrenPrefix + "└── ", childrenPrefix + "    ");
      }
    }
  }
}
