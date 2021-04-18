package com.material.fs.filesystem;

import com.material.fs.exceptions.InvalidFileTypeException;


public abstract class File implements Comparable<File> {
  protected Directory _parent;
  protected String _name;

  File(Directory parent, String name) {
    _parent = parent;
    _name = name;
  }

  public Directory getParent() {
    return _parent;
  }

  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
  }

  // TODO update this with external comparator
  @Override
  public int compareTo(File o) {
    return _name.compareTo(o._name);
  }

  public File move(Directory newParent) {
    _parent = newParent;
    return this;
  }

  abstract File deepCopy();

  public String print() {
    StringBuilder stringBuilder = new StringBuilder();
    print(stringBuilder, "", "");
    return stringBuilder.toString();
  }

  @Override
  public String toString() {
    return _name;
  }

  protected void print(StringBuilder builder, String prefix, String childrenPrefix) {
    builder.append(prefix)
        .append(_name)
        .append("\n");
  }

  public ContentFile getContentFile() {
    if (!(this instanceof ContentFile)) {
      throw new InvalidFileTypeException(String.format("%s is not a content file.", getName()));
    }
    return (ContentFile) this;
  }

  public Directory getDirectory() {
    if (!(this instanceof Directory)) {
      throw new InvalidFileTypeException(String.format("%s is not a directory.", getName()));
    }
    return (Directory) this;
  }
}
