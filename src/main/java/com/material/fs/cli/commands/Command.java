package com.material.fs.cli.commands;

import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.Filesystem;


public abstract class Command {
  abstract CommandResponse run(String[] params, Directory cwg, Filesystem filesystem);

  public final CommandResponse runCommand(String[] params, Directory cwg, Filesystem filesystem) {
    try {
      return run(params, cwg, filesystem);
    } catch (Exception e) {
      return new CommandResponse(String.format("Issue running command %s: %s", getName(), e.getMessage()), true, cwg);
    }
  }

  abstract String getName();
}
