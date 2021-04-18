package com.material.fs.cli;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import jline.console.ConsoleReader;


/**
 * This is the main executable for the filesystem's CLI
 *
 * This class handles reading in inputs, passing them to the {@link StateManager}, and then printing it's result.
 */
public class CLI {

  public static void main(String... args) {
    try {

      StateManager stateManager = new StateManager();
      InputStream inStream = new FileInputStream(FileDescriptor.in);
      ConsoleReader reader = new ConsoleReader("App", inStream, System.out, null);

      reader.setPrompt(String.format("%s > ", stateManager.getCwd()));

      String line;

      while ((line = reader.readLine()) != null) {
        System.out.println(stateManager.runCommand(line.split("\\s+")));
        reader.setPrompt(String.format("%s > ", stateManager.getCwd()));
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
  }
}
