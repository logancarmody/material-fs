package com.material.fs.filesystem;

import com.material.fs.filesystem.exceptions.BadPathException;
import com.material.fs.filesystem.exceptions.FileExistsException;
import com.material.fs.filesystem.exceptions.IllegalOperationException;
import com.material.fs.filesystem.exceptions.InvalidFileNameException;
import com.material.fs.filesystem.models.ContentFile;
import com.material.fs.filesystem.models.Directory;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Unit tests for file and directory create functionality
 */
public class CreateTest extends FileSystemBaseTest {

  @Test
  public void testCreateFile() {
    FileSystem filesystem = buildTestFileSystem();
    Directory parentDir = filesystem._root.getChildDirectory("documents");

    ContentFile contentFile = filesystem.createFile(parentDir, "newfile.txt", false, false);
    assertEquals(parentDir.getChildFile("newfile.txt").get(), contentFile);
  }

  @Test
  public void testCreateFileBadName() {
    FileSystem filesystem = buildTestFileSystem();
    Directory parentDir = filesystem._root.getChildDirectory("documents");

    assertExceptionThrown(() -> filesystem.createFile(parentDir, "newfile", false, false),
        InvalidFileNameException.class);
  }

  @Test
  public void testCreateFileAlongPath() {
    FileSystem filesystem = buildTestFileSystem();
    Directory parentDir = filesystem._root.getChildDirectory("documents");

    ContentFile contentFile = filesystem.createFile(parentDir, "newDir/newfile.txt", true, false);
    assertEquals(parentDir.getChildDirectory("newDir").getChildFile("newfile.txt").get(), contentFile);
  }

  @Test
  public void testExceptionCreateFileAlongPath() {
    FileSystem filesystem = buildTestFileSystem();
    Directory parentDir = filesystem._root.getChildDirectory("documents");

    assertExceptionThrown(() -> filesystem.createFile(parentDir, "newDir/newfile.txt", false, false),
        BadPathException.class);
  }

  @Test
  public void testExceptionCreateExistingFile() {
    FileSystem filesystem = buildTestFileSystem();
    Directory parentDir = filesystem._root.getChildDirectory("documents");

    assertExceptionThrown(() -> filesystem.createFile(parentDir, "paper.txt", false, false),
        FileExistsException.class);
  }

  @Test
  public void testCreateDirectory() {
    FileSystem filesystem = buildTestFileSystem();
    Directory parentDir = filesystem._root.getChildDirectory("documents");

    Directory newDirectory = filesystem.createDirectory(parentDir, "newDir", false);
    assertEquals(parentDir.getChildDirectory("newDir"), newDirectory);
  }

  @Test
  public void testCreateDirectoryAlongPath() {
    FileSystem filesystem = buildTestFileSystem();
    Directory parentDir = filesystem._root.getChildDirectory("documents");

    Directory newDirectory = filesystem.createDirectory(parentDir, "newDir/anotherNewDir", true);
    assertEquals(parentDir.getChildDirectory("newDir").getChildDirectory("anotherNewDir"), newDirectory);
  }

  @Test
  public void testCreateDirectoryAlongPathException() {
    FileSystem filesystem = buildTestFileSystem();
    Directory parentDir = filesystem._root.getChildDirectory("documents");

    assertExceptionThrown(() -> filesystem.createDirectory(parentDir, "newDir/anotherNewDir", false),
        BadPathException.class);
  }

  @Test
  public void testCreateDirectoryWithFileNameException() {
    FileSystem filesystem = buildTestFileSystem();
    Directory parentDir = filesystem._root.getChildDirectory("documents");

    assertExceptionThrown(() -> filesystem.createDirectory(parentDir, "newDir.txt", false),
        IllegalOperationException.class);
  }

  @Test
  public void testCreateDirectoryWithExistingDir() {
    FileSystem filesystem = buildTestFileSystem();
    Directory parentDir = filesystem._root.getChildDirectory("documents");

    assertExceptionThrown(() -> filesystem.createDirectory(parentDir, "secrets", false),
        FileExistsException.class);
  }
}
