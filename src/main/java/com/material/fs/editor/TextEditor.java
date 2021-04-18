package com.material.fs.editor;

import com.material.fs.filesystem.models.ContentFile;


public class TextEditor {

  /**
   * Renders a {@link EditorView}.
   * This method will block until the editing is complete.
   * For a single-threaded file-system, this is just to prevent further changes while editing a file.
   */
  public void blockingRenderFile(ContentFile file) {
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
