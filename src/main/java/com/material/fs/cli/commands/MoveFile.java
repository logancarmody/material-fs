package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.FileSystem;
import java.util.Optional;


/**
 * This command allows you to move files from one location to another.
 * Syntax: "move --file <file to copy> --destination <destination> --name <[optional] new name> -r"
 */
public class MoveFile extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwd, FileSystem filesystem) {
    MoveFileCommand moveFileCommand = new MoveFileCommand();

    JCommander jCommander = JCommander.newBuilder()
        .addCommand(getName(), moveFileCommand)
        .build();

    jCommander.parse(params);

    filesystem.moveFile(cwd, moveFileCommand.startingFile, moveFileCommand.endDirectory,
        Optional.ofNullable(moveFileCommand.newName), moveFileCommand.recursive);

    return new CommandResponse("Moved file successfully", false, cwd);
  }

  @Override
  public String getName() {
    return "move";
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
