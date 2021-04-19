package com.material.fs.filesystem;

import com.google.common.collect.ImmutableList;
import com.material.fs.filesystem.exceptions.BadPathException;
import com.material.fs.filesystem.exceptions.IllegalOperationException;
import com.material.fs.filesystem.exceptions.InvalidFileTypeException;
import com.material.fs.filesystem.models.ContentFile;
import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

import static org.junit.Assert.*;


public class GeneralFilesystemTest extends FileSystemBaseTest {
  @Test
  public void testCwg() {
    FileSystem filesystem = buildTestFileSystem();
    Directory directory = filesystem._root.getChildDirectory("documents")
        .getChildDirectory("secrets");

    assertEquals(filesystem.currentFullPath(directory), "~/documents/secrets");
  }

  @Test
  public void testGetFile() {
    FileSystem filesystem = buildTestFileSystem();
    ContentFile expected = filesystem._root.getChildDirectory("documents")
        .getChildDirectory("secrets").getChildFile("secrets.txt").get().getContentFile();

    assertEquals(filesystem.getContentFileAtPath(filesystem._root, "~/documents/secrets/secrets.txt"),
        expected);
  }

  @Test
  public void testGetFileBadPath() {
    FileSystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> filesystem.getContentFileAtPath(filesystem._root, "~/documents/secrets/notReal.txt"),
        BadPathException.class);
  }

  @Test
  public void testGetContentFileDirectory() {
    FileSystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> filesystem.getContentFileAtPath(filesystem._root, "~/documents/secrets"),
        InvalidFileTypeException.class);
  }

  @Test
  public void testGetDirsChildren() {
    FileSystem filesystem = buildTestFileSystem();
    List<ContentFile> expected = ImmutableList.of(filesystem._root.getChildDirectory("documents")
        .getChildDirectory("secrets").getChildFile("secrets.txt").get().getContentFile());

    assertEquals(filesystem.getDirsChildren(filesystem._root, "~/documents/secrets").toArray(),
        expected.toArray());
  }

  @Test
  public void testGetDirsChildrenRoot() {
    FileSystem filesystem = buildTestFileSystem();
    List<File> expected = ImmutableList.of(filesystem._root.getChildDirectory("documents"),
        filesystem._root.getChildDirectory("programs"),
        filesystem._root.getChildFile("root.txt").get());

    assertEquals(filesystem.getDirsChildren(filesystem._root).toArray(),
        expected.toArray());
  }

  @Test
  public void testSearch() {
    FileSystem filesystem = buildTestFileSystem();
    Optional<String> result = filesystem.search(filesystem._root, "txt", false);

    assertEquals(result.get(), "root.txt\n" + "documents/paper.txt\n" + "documents/readme.txt\n"
        + "documents/secrets/secrets.txt");
  }

  @Test
  public void testSearchFindFirst() {
    FileSystem filesystem = buildTestFileSystem();
    Optional<String> result = filesystem.search(filesystem._root, "txt", true);

    assertEquals(result.get(), "root.txt");
  }

  @Test
  public void testPrint() {
    FileSystem filesystem = buildTestFileSystem();

    String printResult = filesystem.print(filesystem._root);
    assertEquals(printResult,
        "~\n"
            + "|-- documents\n"
            + "│   |-- paper.txt\n"
            + "│   |-- readme.txt\n"
            + "│   └── secrets\n"
            + "│       └── secrets.txt\n"
            + "|-- programs\n"
            + "│   └── program.exec\n"
            + "└── root.txt\n");
  }

  @Test
  public void testDelete() {
    FileSystem filesystem = buildTestFileSystem();

    filesystem.deleteFile(filesystem._root, "~/documents/paper.txt", false);

    assertFalse(filesystem._root.getChildDirectory("documents").getChildFile("paper.txt").isPresent());
  }

  @Test
  public void testDeleteDirectory() {
    FileSystem filesystem = buildTestFileSystem();

    filesystem.deleteFile(filesystem._root, "~/documents", true);

    assertFalse(filesystem._root.getChildFile("documents").isPresent());
  }

  @Test
  public void testDeleteDirectoryError() {
    FileSystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> {
      filesystem.deleteFile(filesystem._root, "~/documents", false);
      return null;
    }, IllegalOperationException.class);
  }
}
