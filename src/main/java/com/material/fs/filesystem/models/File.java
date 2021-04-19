package com.material.fs.filesystem.models;

import com.material.fs.filesystem.exceptions.InvalidFileTypeException;


/**
 * Base File class for the Filesystem
 */
public abstract class File implements Comparable<File> {
  protected Directory _parent;
  protected String _name;

  File(Directory parent, String name) {
    _parent = parent;
    _name = name;
  }

  /**
   * @return Gets the files parent Directory
   */
  public Directory getParent() {
    return _parent;
  }

  /**
   * @return the files name
   */
  public String getName() {
    return _name;
  }

  /**
   * Change the files name
   */
  public void setName(String name) {
    _name = name;
  }

  @Override
  public int compareTo(File o) {
    // Only need to compare names since those should be unique in the context of a directory
    return _name.compareTo(o._name);
  }

  /**
   * Move the childs parent reference to a different parent.
   * Note: you still have to perform the action on the parent itself
   */
  public File move(Directory newParent) {
    _parent = newParent;
    return this;
  }

  /**
   * @return a deep copy of this file. If this is a directory, that means new children as well
   */
  public abstract File deepCopy();

  /**
   * Print this file and it's children recursively, displaying a directory structure
   */
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

  /**
   * @return true if this is a {@link ContentFile}
   */
  public boolean isContentFile() {
    return this instanceof ContentFile;
  }

  /**
   * @return {@link ContentFile} instance of this
   * @throws {@link InvalidFileTypeException} when this is not an instance of a ContentFile
   */
  public ContentFile getContentFile() {
    if (!isContentFile()) {
      throw new InvalidFileTypeException(String.format("%s is not a content file.", getName()));
    }
    return (ContentFile) this;
  }

  /**
   * @return true if this is a {@link Directory}
   */
  public boolean isDirectory() {
    return this instanceof Directory;
  }

  /**
   * @return {@link Directory} instance of this
   * @throws {@link InvalidFileTypeException} when this is not an instance of a Directory
   */
  public Directory getDirectory() {
    if (!isDirectory()) {
      throw new InvalidFileTypeException(String.format("%s is not a directory.", getName()));
    }
    return (Directory) this;
  }
}
