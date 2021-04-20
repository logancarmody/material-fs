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

  /**
   * This handles commands from STDIN. It handles compound commands using "&&"
   *
   * @param reader the console reader
   * @param stateManager the state manager
   * @throws IOException when the console reader throws an IOException
   */
  public static void handleCommands(ConsoleReader reader, StateManager stateManager) throws IOException {
    reader.setPrompt(String.format("%s > ", stateManager.getCwd()));

    String line;

    while ((line = reader.readLine()) != null) {
      String[] commands = line.split(" && ");
      for (String command : commands) {
        System.out.println(stateManager.runCommand(command.split("\\s+")));
      }

      reader.setPrompt(String.format("%s > ", stateManager.getCwd()));
    }
  }

  public static void main(String... args) {
    try {

      StateManager stateManager = new StateManager();
      InputStream inStream = new FileInputStream(FileDescriptor.in);
      ConsoleReader reader = new ConsoleReader("App", inStream, System.out, null);

      System.out.println(stateManager.getWelcomeMessage());
      handleCommands(reader, stateManager);

    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.exit(1);
    }
  }
}
