package com.material.fs.filesystem;

import com.material.fs.filesystem.exceptions.BadPathException;
import com.material.fs.filesystem.exceptions.IllegalOperationException;
import com.material.fs.filesystem.exceptions.InvalidFileNameException;
import java.util.Optional;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Tests for copying files in the FileSystem
 */
public class CopyTest extends FileSystemBaseTest {
  @Test
  public void testCopyFile() {
    FileSystem filesystem = buildTestFileSystem();

    filesystem.copyFile(filesystem._root, "~/documents/paper.txt", "~", Optional.empty(), false);

    assertTrue(filesystem._root.getChildFile("paper.txt").isPresent());
    assertTrue(filesystem._root.getChildDirectory("documents").getChildFile("paper.txt").isPresent());
  }

  @Test
  public void testCopyFileName() {
    FileSystem filesystem = buildTestFileSystem();

    filesystem.copyFile(filesystem._root, "~/documents/paper.txt", "~", Optional.of("cool.t"), false);

    assertTrue(filesystem._root.getChildFile("cool.t").isPresent());
    assertTrue(filesystem._root.getChildDirectory("documents").getChildFile("paper.txt").isPresent());
  }

  @Test
  public void testCopyDirectory() {
    FileSystem filesystem = buildTestFileSystem();

    filesystem.copyFile(filesystem._root, "~/documents/secrets", "~", Optional.empty(), true);

    assertTrue(filesystem._root.getChildDirectory("secrets").getChildFile("secrets.txt").isPresent());
    assertTrue(filesystem._root.getChildDirectory("documents").getContentMap().containsKey("secrets"));
  }

  @Test
  public void testCopyDirectoryName() {
    FileSystem filesystem = buildTestFileSystem();

    filesystem.copyFile(filesystem._root, "~/documents/secrets", "~", Optional.of("newSecrets"), true);

    assertTrue(filesystem._root.getChildDirectory("newSecrets").getChildFile("secrets.txt").isPresent());
    assertTrue(filesystem._root.getChildDirectory("documents").getContentMap().containsKey("secrets"));
  }

  @Test
  public void testCopyDirectoryNoRecursive() {
    FileSystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> {
          filesystem.copyFile(filesystem._root, "~/documents/secrets", "~", Optional.empty(), false);
          return null;
        },
        IllegalOperationException.class);
  }

  @Test
  public void testCopyCurrentDirectory() {
    FileSystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> {
          filesystem.copyFile(filesystem._root.getChildDirectory("documents").getChildDirectory("secrets"),
              "~/documents/secrets", "~", Optional.empty(), true);
          return null;
        },
        IllegalOperationException.class);
  }

  @Test
  public void testCopyNotRealFile() {
    FileSystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> {
          filesystem.copyFile(filesystem._root,
              "~/foo/secrets", "~", Optional.empty(), true);
          return null;
        },
        BadPathException.class);
  }

  @Test
  public void testCopyBadFileNameFile() {
    FileSystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> {
          filesystem.copyFile(filesystem._root,
              "~/documents/secrets", "~", Optional.of("fo.b"), true);
          return null;
        },
        InvalidFileNameException.class);
  }
}
