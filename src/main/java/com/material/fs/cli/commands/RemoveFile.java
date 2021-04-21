package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.FileSystem;


/**
 * This command deletes the specified file.
 * Use -r flag to delete a directory
 */
public class RemoveFile extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwd, FileSystem filesystem) {
    RemoveFileCommand deleteFileCommand = new RemoveFileCommand();
    JCommander jCommander = JCommander.newBuilder()
        .addCommand(getName(), deleteFileCommand)
        .build();

    jCommander.parse(params);
    filesystem.deleteFile(cwd, deleteFileCommand.filePath, deleteFileCommand.recursive);

    return new CommandResponse("Removed File", false, cwd);
  }

  @Override
  public String getName() {
    return "rm";
  }

  private class RemoveFileCommand extends HelperCommand {
    @Parameter(description = "Path to the file to create", required = true)
    private String filePath;

    @Parameter(names = "-r", description = "If you should delete a directory")
    private boolean recursive = false;
  }
}
