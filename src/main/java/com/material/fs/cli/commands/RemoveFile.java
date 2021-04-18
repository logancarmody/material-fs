package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.Filesystem;


public class RemoveFile extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwg, Filesystem filesystem) {
    RemoveFileCommand deleteFileCommand = new RemoveFileCommand();
    JCommander jCommander = JCommander.newBuilder()
        .addCommand("rm", deleteFileCommand)
        .build();

    jCommander.parse(params);
    filesystem.deleteFile(cwg, deleteFileCommand.filePath, deleteFileCommand.recursive);

    return new CommandResponse("Removed File", false, cwg);
  }

  @Override
  public String getName() {
    return null;
  }

  private class RemoveFileCommand {
    @Parameter(description = "Path to the file to create", required = true)
    private String filePath;

    @Parameter(names = "-r", description = "If you should delete a directory")
    private boolean recursive = false;
  }
}
