package com.material.fs.filesystem;

import com.material.fs.filesystem.exceptions.BadPathException;
import com.material.fs.filesystem.exceptions.IllegalOperationException;
import com.material.fs.filesystem.exceptions.InvalidFileNameException;
import java.util.Optional;
import org.junit.Test;

import static org.junit.Assert.*;


public class MoveTest extends FileSystemBaseTest {
  @Test
  public void testMoveFile() {
    Filesystem filesystem = buildTestFileSystem();

    filesystem.moveFile(filesystem._root, "~/documents/paper.txt", "~", Optional.empty(), false);

    assertTrue(filesystem._root.getChildFile("paper.txt").isPresent());
    assertFalse(filesystem._root.getChildDirectory("documents").getChildFile("paper.txt").isPresent());
  }

  @Test
  public void testMoveFileName() {
    Filesystem filesystem = buildTestFileSystem();

    filesystem.moveFile(filesystem._root, "~/documents/paper.txt", "~", Optional.of("cool.t"), false);

    assertTrue(filesystem._root.getChildFile("cool.t").isPresent());
    assertFalse(filesystem._root.getChildDirectory("documents").getChildFile("paper.txt").isPresent());
  }

  @Test
  public void testMoveDirectory() {
    Filesystem filesystem = buildTestFileSystem();

    filesystem.moveFile(filesystem._root, "~/documents/secrets", "~", Optional.empty(), true);

    assertTrue(filesystem._root.getChildDirectory("secrets").getChildFile("secrets.txt").isPresent());
    assertFalse(filesystem._root.getChildDirectory("documents").getContentMap().containsKey("secrets"));
  }

  @Test
  public void testMoveDirectoryName() {
    Filesystem filesystem = buildTestFileSystem();

    filesystem.moveFile(filesystem._root, "~/documents/secrets", "~", Optional.of("newSecrets"), true);

    assertTrue(filesystem._root.getChildDirectory("newSecrets").getChildFile("secrets.txt").isPresent());
    assertFalse(filesystem._root.getChildDirectory("documents").getContentMap().containsKey("secrets"));
  }

  @Test
  public void testMoveDirectoryNoRecursive() {
    Filesystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> {
          filesystem.moveFile(filesystem._root, "~/documents/secrets", "~", Optional.empty(), false);
          return null;
        },
        IllegalOperationException.class);
  }

  @Test
  public void testMoveCurrentDirectory() {
    Filesystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> {
          filesystem.moveFile(filesystem._root.getChildDirectory("documents").getChildDirectory("secrets"),
              "~/documents/secrets", "~", Optional.empty(), true);
          return null;
        },
        IllegalOperationException.class);
  }

  @Test
  public void testMoveNotRealFile() {
    Filesystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> {
          filesystem.moveFile(filesystem._root,
              "~/foo/secrets", "~", Optional.empty(), true);
          return null;
        },
        BadPathException.class);
  }

  @Test
  public void testMoveBadFileNameFile() {
    Filesystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> {
          filesystem.moveFile(filesystem._root,
              "~/documents/secrets", "~", Optional.of("fo.b"), true);
          return null;
        },
        InvalidFileNameException.class);
  }
}
