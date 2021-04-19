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
import java.util.concurrent.Callable;
import org.junit.Test;

import static org.junit.Assert.*;


public class FileSystemBaseTest {

  @Test
  public void testCwg() {
    Filesystem filesystem = buildTestFileSystem();
    Directory directory = filesystem._root.getChildDirectory("documents")
        .getChildDirectory("secrets");

    assertEquals(filesystem.currentFullPath(directory), "~/documents/secrets");
  }

  @Test
  public void testGetFile() {
    Filesystem filesystem = buildTestFileSystem();
    ContentFile expected = filesystem._root.getChildDirectory("documents")
        .getChildDirectory("secrets").getChildFile("secrets.txt").get().getContentFile();

    assertEquals(filesystem.getContentFileAtPath(filesystem._root, "~/documents/secrets/secrets.txt"),
        expected);
  }

  @Test
  public void testGetFileBadPath() {
    Filesystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> filesystem.getContentFileAtPath(filesystem._root, "~/documents/secrets/notReal.txt"),
        BadPathException.class);
  }

  @Test
  public void testGetContentFileDirectory() {
    Filesystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> filesystem.getContentFileAtPath(filesystem._root, "~/documents/secrets"),
        InvalidFileTypeException.class);
  }

  @Test
  public void testGetDirsChildren() {
    Filesystem filesystem = buildTestFileSystem();
    List<ContentFile> expected = ImmutableList.of(filesystem._root.getChildDirectory("documents")
        .getChildDirectory("secrets").getChildFile("secrets.txt").get().getContentFile());

    assertEquals(filesystem.getDirsChildren(filesystem._root, "~/documents/secrets").toArray(),
        expected.toArray());
  }

  @Test
  public void testGetDirsChildrenRoot() {
    Filesystem filesystem = buildTestFileSystem();
    List<File> expected = ImmutableList.of(filesystem._root.getChildDirectory("documents"),
        filesystem._root.getChildDirectory("programs"),
        filesystem._root.getChildFile("root.txt").get());

    assertEquals(filesystem.getDirsChildren(filesystem._root).toArray(),
        expected.toArray());
  }

  @Test
  public void testSearch() {
    Filesystem filesystem = buildTestFileSystem();
    Optional<String> result = filesystem.search(filesystem._root, "txt", false);

    assertEquals(result.get(), "root.txt\n" + "documents/paper.txt\n" + "documents/readme.txt\n"
        + "documents/secrets/secrets.txt");
  }

  @Test
  public void testSearchFindFirst() {
    Filesystem filesystem = buildTestFileSystem();
    Optional<String> result = filesystem.search(filesystem._root, "txt", true);

    assertEquals(result.get(), "root.txt");
  }

  @Test
  public void testPrint() {
    Filesystem filesystem = buildTestFileSystem();

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
    Filesystem filesystem = buildTestFileSystem();

    filesystem.deleteFile(filesystem._root, "~/documents/paper.txt", false);

    assertFalse(filesystem._root.getChildDirectory("documents").getChildFile("paper.txt").isPresent());
  }

  @Test
  public void testDeleteDirectory() {
    Filesystem filesystem = buildTestFileSystem();

    filesystem.deleteFile(filesystem._root, "~/documents", true);

    assertFalse(filesystem._root.getChildFile("documents").isPresent());
  }

  @Test
  public void testDeleteDirectoryError() {
    Filesystem filesystem = buildTestFileSystem();

    assertExceptionThrown(() -> {
      filesystem.deleteFile(filesystem._root, "~/documents", false);
      return null;
    }, IllegalOperationException.class);
  }

  protected void assertExceptionThrown(Callable<?> callable, Class<? extends RuntimeException> exceptionClass) {
    try {
      callable.call();
    } catch (Exception e) {
      assertTrue(e.getClass().isAssignableFrom(exceptionClass));
      return;
    }
    fail("No exception thrown");
  }

  protected Filesystem buildTestFileSystem() {
    Filesystem filesystem = new Filesystem();
    Directory root = filesystem._root;
    Directory documents = root.createDirectory("documents");
    documents.createEmptyContentFile("paper.txt");
    documents.createEmptyContentFile("readme.txt");
    documents.createDirectory("secrets").createEmptyContentFile("secrets.txt");
    root.createEmptyContentFile("root.txt");
    root.createDirectory("programs").createEmptyContentFile("program.exec");
    return filesystem;
  }
}
