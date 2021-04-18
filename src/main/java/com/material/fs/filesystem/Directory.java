package com.material.fs.filesystem;

import com.material.fs.exceptions.FileExistsException;
import com.material.fs.filesystem.util.Constants;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;


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
  File deepCopy() {
    Map<String, File> newContents = _contents.values().stream()
        .map(File::deepCopy)
        .collect(Collectors.toMap(File::getName, Function.identity(), (left, right) -> left, TreeMap::new));

    return new Directory(_parent, _name, newContents);
  }

  public Collection<File> getContents() {
    return _contents.values();
  }

  public ContentFile createEmptyContentFile(String filename) {
    // TODO should validate file name here?
    if (findChild(filename).isPresent()) {
      throw new RuntimeException();
    }

    ContentFile file = new ContentFile(this, filename);
    _contents.put(file.getName(), file);
    return file;
  }

  public boolean contains(File file) {
    return file.equals(_contents.get(file.getName()));
  }

  public Directory createDirectory(String filename) {
    if (findChild(filename).isPresent()) {
      throw new RuntimeException();
    }

    Directory directory = new Directory(this, filename);
    _contents.put(directory.getName(), directory);
    return directory;
  }

  public Optional<File> findChild(String fileName) {
    if (fileName.equals(Constants.CURRENT_DIRECTORY_SHORTCUT)) {
      return Optional.of(this);
    }

    if (fileName.equals(Constants.PARENT_DIRECTORY_SHORTCUT)) {
      return Optional.ofNullable(getParent());
    }

    return Optional.ofNullable(_contents.get(fileName));
  }

  public Optional<Directory> findDirectory(String fileName) {
    return findChild(fileName)
        .filter(file -> file instanceof Directory)
        .map(file -> (Directory) file);
  }

  public void deleteFile(File file) {
    _contents.remove(file.getName());
    file._parent = null;
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
