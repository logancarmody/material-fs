package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;
import com.material.fs.filesystem.FileSystem;
import java.util.Collection;
import java.util.stream.Collectors;


/**
 * This command lists all files along a path.
 * If the path is not specified, it will do so in the CWD
 */
public class ListFiles extends Command {

  @Override
  public CommandResponse run(String[] params, Directory cwd, FileSystem filesystem) {
    ListFilesCommand listFilesCommand = new ListFilesCommand();
    JCommander jCommander = JCommander.newBuilder()
        .addCommand(getName(), listFilesCommand)
        .build();

    jCommander.parse(params);

    Collection<File> resultingChildren;

    if (listFilesCommand.pathToListFiles != null) {
      resultingChildren = filesystem.getDirsChildren(cwd, listFilesCommand.pathToListFiles);
    } else {
      resultingChildren = filesystem.getDirsChildren(cwd);
    }

    String result = resultingChildren.stream().map(File::toString).collect(Collectors.joining("\t\t"));
    return new CommandResponse(result, false, cwd);
  }

  @Override
  public String getName() {
    return "ls";
  }

  private static class ListFilesCommand {
    @Parameter(description = "The path to list files")
    private String pathToListFiles;
  }
}
