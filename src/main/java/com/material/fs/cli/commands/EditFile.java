package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.FileSystem;


/**
 * This command opens up a file for editing in the editor
 */
public class EditFile extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwd, FileSystem filesystem) {
    EditFileCommand editFileCommand = new EditFileCommand();
    JCommander jCommander = JCommander.newBuilder()
        .addCommand(getName(), editFileCommand)
        .build();

    jCommander.parse(params);
    filesystem.editFile(cwd, editFileCommand.filePath);

    return new CommandResponse("Edited File", false, cwd);
  }

  @Override
  public String getName() {
    return "edit";
  }

  private class EditFileCommand {
    @Parameter(description = "Path to the file to edit", required = true)
    private String filePath;
  }
}
