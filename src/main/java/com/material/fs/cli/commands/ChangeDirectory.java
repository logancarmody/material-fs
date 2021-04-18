package com.material.fs.cli.commands;

import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.Filesystem;


public class ChangeDirectory extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwd, Filesystem filesystem) {
    if (params.length == 1) {
      return new CommandResponse("", false, filesystem.getRootDirectory());
    } else if (params.length == 2) {
      return new CommandResponse("", false, filesystem.getDirectoryAtPath(cwd, params[1]));
    }
    throw new RuntimeException();
  }

  @Override
  public String getName() {
    return null;
  }
}
