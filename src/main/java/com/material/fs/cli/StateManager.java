package com.material.fs.cli;

import com.google.common.collect.ImmutableMap;
import com.material.fs.cli.commands.ChangeDirectory;
import com.material.fs.cli.commands.Command;
import com.material.fs.cli.commands.CommandResponse;
import com.material.fs.cli.commands.CopyFile;
import com.material.fs.cli.commands.CreateFile;
import com.material.fs.cli.commands.EditFile;
import com.material.fs.cli.commands.ListFiles;
import com.material.fs.cli.commands.MakeDirectory;
import com.material.fs.cli.commands.MoveFile;
import com.material.fs.cli.commands.RemoveFile;
import com.material.fs.filesystem.Directory;
import com.material.fs.filesystem.Filesystem;
import java.util.Map;
import java.util.Optional;


public class StateManager {
  private static Map<String, Command> _commandMap = ImmutableMap.<String, Command>builder()
      .put("mkdir", new MakeDirectory())
      .put("ls", new ListFiles())
      .put("cd", new ChangeDirectory())
      .put("create", new CreateFile())
      .put("edit", new EditFile())
      .put("move", new MoveFile())
      .put("copy", new CopyFile())
      .put("rm", new RemoveFile())
      .build();

  private final Filesystem _filesystem;
  private Directory _cwg;

  public StateManager() {
    _filesystem = new Filesystem();
    _cwg = _filesystem.getRootDirectory();
  }

  public String runCommand(String[] commandAry) {
    if (commandAry.length == 0) {
      return "";
    }

    String baseCommand = commandAry[0];

    return Optional.ofNullable(_commandMap.get(baseCommand))
        .map(command -> {
          CommandResponse cr = command.runCommand(commandAry, _cwg, _filesystem);
          _cwg = cr.getCwg();
          return cr.toString();
        })
        .orElseGet(() -> String.format("Error: %s is not a valid command.", baseCommand));
  }

  public Directory getCwg() {
    return _cwg;
  }
}
