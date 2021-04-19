package com.material.fs.cli.commands;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.material.fs.filesystem.Filesystem;
import com.material.fs.filesystem.models.Directory;
import java.util.Optional;


/**
 * This command allows you to search for files by filename.
 * Syntax: "search <search string> --find-first"
 */
public class SearchFiles extends Command {
  @Override
  public CommandResponse run(String[] params, Directory cwd, Filesystem filesystem) {
    SearchCommand searchCommand = new SearchCommand();

    JCommander jCommander = JCommander.newBuilder()
        .addCommand(getName(), searchCommand)
        .build();

    jCommander.parse(params);

    Optional<String> result = filesystem.search(cwd, searchCommand.searchString, searchCommand.listAllResults);

    return new CommandResponse(result.orElse("No files found."), false, cwd);
  }

  @Override
  public String getName() {
    return "search";
  }

  private class SearchCommand {
    @Parameter(description = "The search string", required = true)
    String searchString;
    @Parameter(names = "--find-first")
    boolean listAllResults = false;
  }
}
