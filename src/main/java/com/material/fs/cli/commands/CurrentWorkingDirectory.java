package com.material.fs.cli.commands;

import com.material.fs.filesystem.FileSystem;
import com.material.fs.filesystem.models.Directory;


/**
 * Return the full path of the current working directory
 */
public class CurrentWorkingDirectory extends Command {
  @Override
  protected CommandResponse run(String[] params, Directory cwd, FileSystem filesystem) {
    return new CommandResponse(filesystem.currentFullPath(cwd), false, cwd);
  }

  @Override
  public String getName() {
    return "cwd";
  }
}
