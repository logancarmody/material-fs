package com.material.fs.cli;

import com.material.fs.cli.commands.ChangeDirectory;
import com.material.fs.cli.commands.Command;
import com.material.fs.cli.commands.CommandResponse;
import com.material.fs.cli.commands.CopyFile;
import com.material.fs.cli.commands.CreateFile;
import com.material.fs.cli.commands.CurrentWorkingDirectory;
import com.material.fs.cli.commands.EditFile;
import com.material.fs.cli.commands.ListFiles;
import com.material.fs.cli.commands.MakeDirectory;
import com.material.fs.cli.commands.MoveFile;
import com.material.fs.cli.commands.RemoveFile;
import com.material.fs.cli.commands.SearchFiles;
import com.material.fs.cli.commands.ShowStructure;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.FileSystem;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * This is the core StateManager of the FS CLI.
 * It's responsibility:
 *   - mapping String arrays parsed from {@link CLI} to the proper {@link Command} class
 *   - maintaining the Current Working Directory of the CLI
 *   - parsing the responses from the commands and updating the CWG when necessary
 */
public class StateManager {
  private static Map<String, Command> _commandMap = Stream.of(new MakeDirectory(),
      new ListFiles(),
      new ChangeDirectory(),
      new CreateFile(),
      new EditFile(),
      new MoveFile(),
      new CopyFile(),
      new RemoveFile(),
      new SearchFiles(),
      new CurrentWorkingDirectory(),
      new ShowStructure())
      .collect(Collectors.toMap(Command::getName, Function.identity()));

  private final FileSystem _fileSystem;
  private Directory _cwd;

  public StateManager() {
    _fileSystem = new FileSystem();
    _cwd = _fileSystem.getRootDirectory();
  }

  /**
   * Run the given command
   * @param commandAry String array parsed from the CLI
   * @return the result message
   */
  public String runCommand(String[] commandAry) {
    if (commandAry.length == 0) {
      return "";
    }

    String baseCommand = commandAry[0];

    return Optional.ofNullable(_commandMap.get(baseCommand))
        .map(command -> {
          CommandResponse cr = command.runCommand(commandAry, _cwd, _fileSystem);
          _cwd = cr.getCwg();
          return cr.toString();
        })
        .orElseGet(() -> String.format("Error: %s is not a valid command.", baseCommand));
  }

  /**
   * Get the Current Working Directory
   */
  public Directory getCwd() {
    return _cwd;
  }
}
