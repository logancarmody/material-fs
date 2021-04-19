package com.material.fs.filesystem;

import com.material.fs.filesystem.models.Directory;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;


/**
 * Base FileSystem test class with helper methods
 */
public abstract class FileSystemBaseTest {

  protected void assertExceptionThrown(Callable<?> callable, Class<? extends RuntimeException> exceptionClass) {
    try {
      callable.call();
    } catch (Exception e) {
      assertTrue(e.getClass().isAssignableFrom(exceptionClass));
      return;
    }
    fail("No exception thrown");
  }

  protected FileSystem buildTestFileSystem() {
    FileSystem filesystem = new FileSystem();
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
