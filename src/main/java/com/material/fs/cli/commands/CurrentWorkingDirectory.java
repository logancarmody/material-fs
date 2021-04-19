package com.material.fs.cli.commands;

import com.material.fs.filesystem.Filesystem;
import com.material.fs.filesystem.models.Directory;


public class CurrentWorkingDirectory extends Command {
  @Override
  protected CommandResponse run(String[] params, Directory cwd, Filesystem filesystem) {
    return new CommandResponse(filesystem.currentFullPath(cwd), false, cwd);
  }

  @Override
  public String getName() {
    return "cwd";
  }
}
