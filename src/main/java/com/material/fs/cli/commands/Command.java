package com.material.fs.cli.commands;

import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.FileSystem;


/**
 * Base class representing a CLI Command.
 *
 * Implementations should implement {@link Command#run} with their core logic
 */
public abstract class Command {

  /**
   * Parses and runs the command specified by the params.
   * Should return a CommandResponse for all response types, including errors.
   *
   * @param params the params parsed from the CLI to be interpreted by this command.
   * @param cwd the current working directory
   * @param filesystem the filesystem
   */
  protected abstract CommandResponse run(String[] params, Directory cwd, FileSystem filesystem);

  /**
   * Parses and runs the command specified by the params.
   * Should return a CommandResponse for all response types, including errors.
   *
   * @param params the params parsed from the CLI to be interpreted by this command.
   * @param cwd the current working directory
   * @param filesystem the filesystem
   */
  public final CommandResponse runCommand(String[] params, Directory cwd, FileSystem filesystem) {
    try {
      return run(params, cwd, filesystem);
    } catch (Exception e) {
      return new CommandResponse(String.format("Issue running command %s: %s", getName(), e.getMessage()), true, cwd);
    }
  }

  /**
   * @return the name of the command. This should be the single String to invoke the command from the CLI.
   */
  public abstract String getName();

  protected class HelperCommand {
    @Parameter(help = true)
    private String helpCommand;
  }
}
