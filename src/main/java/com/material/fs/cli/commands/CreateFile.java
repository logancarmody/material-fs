package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.Filesystem;


public class CreateFile extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwg, Filesystem filesystem) {
    CreateFileCommand createFileCommand = new CreateFileCommand();
    JCommander jCommander = JCommander.newBuilder()
        .addCommand("create", createFileCommand)
        .build();

    jCommander.parse(params);
    filesystem.createFile(cwg, createFileCommand.filePath, createFileCommand.createAlongPath, createFileCommand.openForEditing);

    return new CommandResponse("Created File", false, cwg);
  }

  @Override
  public String getName() {
    return null;
  }

  private class CreateFileCommand {
    @Parameter(description = "Path to the file to create", required = true)
    private String filePath;

    @Parameter(names = "-r")
    private boolean createAlongPath = false;

    @Parameter(names = "-o")
    private boolean openForEditing = false;
  }
}
