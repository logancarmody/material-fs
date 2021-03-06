package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.FileSystem;


/**
 * This command allows you to create a new file.
 * Add -r tag to do so recursively (creating directories along the path)
 * Add -o tag to open the file for editing
 */
public class CreateFile extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwd, FileSystem filesystem) {
    CreateFileCommand createFileCommand = new CreateFileCommand();
    JCommander jCommander = JCommander.newBuilder()
        .addCommand(getName(), createFileCommand)
        .build();

    jCommander.parse(params);
    filesystem.createFile(cwd, createFileCommand.filePath, createFileCommand.createAlongPath, createFileCommand.openForEditing);

    return new CommandResponse("Created File", false, cwd);
  }

  @Override
  public String getName() {
    return "create";
  }

  private class CreateFileCommand extends HelperCommand {
    @Parameter(description = "Path to the file to create", required = true)
    private String filePath;

    @Parameter(names = "-r")
    private boolean createAlongPath = false;

    @Parameter(names = "-o")
    private boolean openForEditing = false;
  }
}
