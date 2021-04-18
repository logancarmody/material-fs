package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.Filesystem;


public class EditFile extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwg, Filesystem filesystem) {
    EditFileCommand editFileCommand = new EditFileCommand();
    JCommander jCommander = JCommander.newBuilder()
        .addCommand("edit", editFileCommand)
        .build();

    jCommander.parse(params);
    filesystem.editFile(cwg, editFileCommand.filePath);

    return new CommandResponse("Edited File", false, cwg);
  }

  @Override
  public String getName() {
    return null;
  }

  private class EditFileCommand {
    @Parameter(description = "Path to the file to edit", required = true)
    private String filePath;
  }
}
