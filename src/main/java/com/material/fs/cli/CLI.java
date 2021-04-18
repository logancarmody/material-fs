package com.material.fs.cli;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import jline.console.ConsoleReader;
import jline.console.completer.FileNameCompleter;
import jline.console.completer.StringsCompleter;


public class CLI {

  public static void main(String... args) {
    try {

      StateManager stateManager = new StateManager();
      InputStream inStream = new FileInputStream(FileDescriptor.in);
      ConsoleReader reader = new ConsoleReader("App", inStream, System.out, null);

      reader.setPrompt(String.format("%s > ", stateManager.getCwg()));

      reader.addCompleter(new FileNameCompleter());
      reader.addCompleter(new StringsCompleter(Arrays.asList(new String[]{"cmd1", "exit", "quit",})));

      String line;
      PrintWriter out = new PrintWriter(reader.getOutput());

      while ((line = reader.readLine()) != null) {
        System.out.println(stateManager.runCommand(line.split("\\s+")));
        reader.setPrompt(String.format("%s > ", stateManager.getCwg()));
      }
    } catch (IOException e) {
      System.exit(1);
    }
  }
}
