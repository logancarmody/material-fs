package com.material.fs.editor;

import com.material.fs.filesystem.ContentFile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;


public class EditorView extends JFrame implements ActionListener {
  // Text component
  JTextArea _textArea;

  // Frame
  JFrame _frame;

  private ContentFile _contentFile;

  private boolean _isEditing;

  public EditorView(ContentFile contentFile) {
    _contentFile = contentFile;
    _isEditing = true;

    // Create a frame
    _frame = new JFrame("Edit: " + contentFile.getName());

    try {
      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

      MetalLookAndFeel.setCurrentTheme(new OceanTheme());
    }
    catch (Exception e) {
    }

    // Text component
    _textArea = new JTextArea(_contentFile.getContent());

    // Create a menubar
    JMenuBar mb = new JMenuBar();

    // Create amenu for menu
    JMenuItem saveItem = new JMenuItem("Save");

    JMenuItem closeItem = new JMenuItem("Close");

    closeItem.addActionListener(this);
    saveItem.addActionListener(this);

    mb.add(saveItem);
    mb.add(closeItem);

    _frame.setJMenuBar(mb);
    _frame.add(_textArea);
    _frame.setSize(500, 500);
    _frame.show();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String s = e.getActionCommand();
    if (s.equals("Save")) {
      _contentFile.setContent(_textArea.getText());
    } else if (s.equals("Close")) {
      _frame.dispose();
      _isEditing = false;
    }
  }

  public boolean isEditing() {
    return _isEditing;
  }
}
