package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.FileSystem;


/**
 * This directory changes the current working directory
 */
public class ChangeDirectory extends Command {

  @Override
  public CommandResponse run(String[] params, Directory cwd, FileSystem filesystem) {
    ChangeDirectoryCommand changeDirectoryCommand = new ChangeDirectoryCommand();

    JCommander jCommander = JCommander.newBuilder()
        .addCommand(getName(), changeDirectoryCommand)
        .build();

    jCommander.parse(params);

    if (changeDirectoryCommand.path == null) {
      return new CommandResponse("", false, filesystem.getRootDirectory());
    } else {
      return new CommandResponse("", false, filesystem.getDirectoryAtPath(cwd, changeDirectoryCommand.path));
    }
  }

  @Override
  public String getName() {
    return "cd";
  }

  private class ChangeDirectoryCommand {
    @Parameter(description = "the path to change the directory to. Default is root.")
    private String path;
  }
}
