package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.Filesystem;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;
import java.util.Collection;
import java.util.stream.Collectors;


public class ShowStructure extends Command {

  @Override
  public CommandResponse run(String[] params, Directory cwd, Filesystem filesystem) {
    ShowStructureCommand showStructureCommand = new ShowStructureCommand();
    JCommander jCommander = JCommander.newBuilder()
        .addCommand(getName(), showStructureCommand)
        .build();

    jCommander.parse(params);

    String result;

    if (showStructureCommand.pathToFile != null) {
      result = filesystem.print(cwd, showStructureCommand.pathToFile);
    } else {
      result = filesystem.print(cwd);
    }

    return new CommandResponse(result, false, cwd);
  }

  @Override
  public String getName() {
    return "show";
  }

  private static class ShowStructureCommand {
    @Parameter(description = "The path to list files")
    private String pathToFile;
  }
}
