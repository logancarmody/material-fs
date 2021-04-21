package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.FileSystem;
import com.material.fs.filesystem.models.Directory;


/**
 * This command prints the filesystem structure from the current directory or from a specified directory
 */
public class ShowStructure extends Command {

  @Override
  public CommandResponse run(String[] params, Directory cwd, FileSystem filesystem) {
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

  private class ShowStructureCommand extends HelperCommand {
    @Parameter(description = "The path to list files. Default is cwd")
    private String pathToFile;
  }
}
