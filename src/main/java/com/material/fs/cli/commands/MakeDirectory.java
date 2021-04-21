package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.FileSystem;


/**
 * This directory creates a directory at the given path.
 * The -r flag allows you to do so recursively
 */
public class MakeDirectory extends Command {

  @Override
  public CommandResponse run(String[] params, Directory cwd, FileSystem filesystem) {
    MakeDirectoryCommand makeDirectoryCommand = new MakeDirectoryCommand();
    JCommander jCommander = JCommander.newBuilder()
        .addCommand(getName(), makeDirectoryCommand)
        .build();

    jCommander.parse(params);

    filesystem.createDirectory(cwd, makeDirectoryCommand.pathToCreate, makeDirectoryCommand.recursive);
    return new CommandResponse(String.format("Created directory: %s", makeDirectoryCommand.pathToCreate), false, cwd);
  }

  @Override
  public String getName() {
    return "mkdir";
  }

  private class MakeDirectoryCommand extends HelperCommand {
    @Parameter(description = "The file to create", required = true)
    private String pathToCreate;

    @Parameter(names = "-r", description = "Whether to recursively make directories")
    private boolean recursive = false;
  }
}
