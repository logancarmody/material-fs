package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.File;
import com.material.fs.filesystem.Filesystem;
import java.util.Collection;
import java.util.stream.Collectors;


public class ListFiles extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwg, Filesystem filesystem) {
    ListFilesCommand listFilesCommand = new ListFilesCommand();
    JCommander jCommander = JCommander.newBuilder()
        .addCommand("ls", listFilesCommand)
        .build();

    jCommander.parse(params);

    Collection<File> resultingChildren;

    if (listFilesCommand.pathToListFiles != null) {
      resultingChildren = filesystem.getDirsChildren(cwg, listFilesCommand.pathToListFiles);
    } else {
      resultingChildren = filesystem.getDirsChildren(cwg);
    }

    String result = resultingChildren.stream().map(File::toString).collect(Collectors.joining("\t\t"));
    return new CommandResponse(result, false, cwg);
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
