package com.material.fs.filesystem.models;

import java.util.Objects;


/**
 * A file which contains content
 */
public class ContentFile extends File {
  private String _content;

  ContentFile(Directory parent, String name) {
    this(parent, name, "");
  }

  ContentFile(Directory parent, String name, String contents) {
    super(parent, name);
    _content = contents;
  }

  @Override
  public File deepCopy() {
    return new ContentFile(_parent, _name, _content);
  }

  /**
   * Return the content in the file
   */
  public String getContent() {
    return _content;
  }

  /**
   * Set the content of the file
   * @param newContent the content
   */
  public void setContent(String newContent) {
    _content = newContent;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ContentFile)) {
      return false;
    }

    ContentFile other = (ContentFile) o;

    return Objects.equals(this._parent, other._parent)
        && Objects.equals(this._name, other._name)
        && Objects.equals(this._content, other._content);
  }
}
