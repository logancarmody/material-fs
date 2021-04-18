package com.material.fs.editor;

import com.material.fs.filesystem.models.ContentFile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;


/**
 * Text editor UI class
 * This will launch a JFrame text editor
 */
public class EditorView extends JFrame implements ActionListener {
  private static final String SAVE = "Save";
  private static final String CLOSE = "Close";

  private JTextArea _textArea;
  private JFrame _frame;
  private ContentFile _contentFile;
  private boolean _isEditing;

  public EditorView(ContentFile contentFile) {
    _contentFile = contentFile;
    _isEditing = true;
    _frame = new JFrame("Edit: " + contentFile.getName());

    try {
      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

      MetalLookAndFeel.setCurrentTheme(new OceanTheme());
    }
    catch (Exception e) {
      //swallow
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
    if (s.equals(SAVE)) {
      _contentFile.setContent(_textArea.getText());
    } else if (s.equals(CLOSE)) {
      _frame.dispose();
      _isEditing = false;
    }
  }

  /**
   * Returns true if the user has not hit the closed button yet.
   */
  public boolean isEditing() {
    return _isEditing;
  }
}
