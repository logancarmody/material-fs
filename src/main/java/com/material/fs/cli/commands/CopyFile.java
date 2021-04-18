package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.Filesystem;
import java.util.Optional;


/**
 * This command allows you to copy files from one location to another.
 * Syntax: "copy --file <file to copy> --destination <destination> --name <[optional] new name> -r"
 */
public class CopyFile extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwd, Filesystem filesystem) {
    CopyFileCommand copyFileCommand = new CopyFileCommand();

    JCommander jCommander = JCommander.newBuilder()
        .addCommand("copy", copyFileCommand)
        .build();

    jCommander.parse(params);

    filesystem.copyFile(cwd, copyFileCommand.startingFile, copyFileCommand.endDirectory,
        Optional.ofNullable(copyFileCommand.newName), copyFileCommand.recursive);

    return new CommandResponse("Copied file successfully", false, cwd);
  }

  @Override
  public String getName() {
    return null;
  }

  private class CopyFileCommand {
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
