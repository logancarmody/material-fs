package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.Filesystem;
import java.util.Optional;


public class MoveFile extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwg, Filesystem filesystem) {
    MoveFileCommand moveFileCommand = new MoveFileCommand();

    JCommander jCommander = JCommander.newBuilder()
        .addCommand("move", moveFileCommand)
        .build();

    jCommander.parse(params);

    filesystem.moveFile(cwg, moveFileCommand.startingFile, moveFileCommand.endDirectory,
        Optional.ofNullable(moveFileCommand.newName), moveFileCommand.recursive);

    return new CommandResponse("Moved file successfully", false, cwg);
  }

  @Override
  public String getName() {
    return null;
  }

  private class MoveFileCommand {
    @Parameter(names = "--file", required = true)
    String startingFile;
    @Parameter(names = "--destination", required = true)
    String endDirectory;
    @Parameter(names = "--name")
    String newName;
    @Parameter(names = "-r")
    boolean recursive = false;
  }
}
