package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.Filesystem;


public class MakeDirectory extends Command {

  @Override
  public CommandResponse run(String[] params, Directory cwd, Filesystem filesystem) {
    MakeDirectoryCommand makeDirectoryCommand = new MakeDirectoryCommand();
    JCommander jCommander = JCommander.newBuilder()
        .addCommand("mkdir", makeDirectoryCommand)
        .build();

    jCommander.parse(params);

    filesystem.createDirectory(cwd, makeDirectoryCommand.pathToCreate, makeDirectoryCommand.recursive);
    return new CommandResponse(String.format("Created directory: %s", makeDirectoryCommand.pathToCreate), false, cwd);
  }

  @Override
  public String getName() {
    return "mkdir";
  }

  private static class MakeDirectoryCommand {
    @Parameter(description = "The file to create")
    private String pathToCreate;

    @Parameter(names = "-r", description = "Whether to recursively make directories")
    private boolean recursive = false;
  }
}
