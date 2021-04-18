package com.material.fs.editor;

import com.material.fs.filesystem.ContentFile;


public class TextEditor {

  public void renderFile(ContentFile file) {
    EditorView editorView = new EditorView(file);

    // Prevent taking other FS actions while editing a file
    while (editorView.isEditing()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        return;
      }
    }
  }
}
