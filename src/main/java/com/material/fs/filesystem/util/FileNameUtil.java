package com.material.fs.filesystem.util;

import java.util.regex.Pattern;


public class FileNameUtil {
  private static final String FILE_PATH_REGEX = "\\.\\.|\\.|~|((~/)?([-_A-Za-z0-9]+/)*([-_A-Za-z0-9]+\\.[a-z]+)?)";
  private static final String DIRECTORY_PATH_REGEX = "(~/)?([-_A-Za-z0-9]+/)*([-_A-Za-z0-9]+)?";
  private static final Pattern FILE_PATH_PATTERN = Pattern.compile(FILE_PATH_REGEX);
  private static final Pattern DIRECTORY_PATH_PATTERN = Pattern.compile(DIRECTORY_PATH_REGEX);

  public static boolean isValidFilePath(String maybePath) {
    return FILE_PATH_PATTERN.matcher(maybePath).matches();
  }

  public static boolean isValidDirectoryPath(String maybePath) {
    return DIRECTORY_PATH_PATTERN.matcher(maybePath).matches();
  }

  public static boolean isValidPath(String maybePath) {
    return isValidFilePath(maybePath) || isValidDirectoryPath(maybePath);
  }

  public static boolean isAbsolutePath(String path) {
    return isValidPath(path) && path.startsWith("~");
  }
}
