package com.material.fs.filesystem;

import com.material.fs.filesystem.models.Directory;
import com.material.fs.filesystem.models.File;
import org.junit.Test;


public class FileSystemTest {

  @Test
  public void testCwg() {
    Filesystem filesystem = buildTestFileSystem();
    File file = filesystem._root.findDirectory("documents")
        .flatMap(directory -> directory.findDirectory("secrets"))
        .flatMap(directory -> directory.findChild("secrets.txt"))
        .get();

    System.out.println(filesystem.cwd(file));
  }

  @Test
  public void testGetFile() {
    Filesystem filesystem = buildTestFileSystem();
    File file = filesystem._root.findDirectory("documents")
        .flatMap(directory -> directory.findDirectory("secrets"))
        .flatMap(directory -> directory.findChild("secrets.txt"))
        .get();

    filesystem.getContentFileAtPath(filesystem._root, "~/documents/secrets/secrets.txt");

    System.out.println(filesystem._root);
  }

  @Test
  public void testGetFileParent() {
    Filesystem filesystem = buildTestFileSystem();
    System.out.println(filesystem._root.print());
    Directory directory = filesystem.getDirectoryAtPath(filesystem._root, "~/documents/secrets");

    System.out.println(filesystem.getDirectoryAtPath(directory, "../.."));
  }

//  @Test
//  public void testMoveFileAbsolute() {
//    Filesystem filesystem = buildTestFileSystem();
//    System.out.println(filesystem._root.print());
//    filesystem.moveFile(filesystem.getRootDirectory(), "~/documents/secrets/secrets.txt", "~/documents/");
//
//    System.out.println(filesystem._root.print());
//  }
//
//  @Test
//  public void testMoveFileNewName() {
//    Filesystem filesystem = buildTestFileSystem();
//    System.out.println(filesystem._root.print());
//    filesystem.moveFile(filesystem.getRootDirectory(), "~/documents/secrets/secrets.txt", "~/documents/newSecrets.txt");
//
//    System.out.println(filesystem._root.print());
//  }
//
//  @Test
//  public void testMoveDirectory() {
//    Filesystem filesystem = buildTestFileSystem();
//    System.out.println(filesystem._root.print());
//    filesystem.moveFile(filesystem.getRootDirectory(), "~/documents/secrets", "~/documents/sample");
//
//    System.out.println(filesystem._root.print());
//  }
//
//  @Test
//  public void testCopyFileAbsolute() {
//    Filesystem filesystem = buildTestFileSystem();
//    System.out.println(filesystem._root.print());
//    filesystem.copyFile(filesystem.getRootDirectory(), "~/documents/secrets/secrets.txt", "~/documents/");
//
//    System.out.println(filesystem._root.print());
//  }
//
//  @Test
//  public void testCopyFileNewName() {
//    Filesystem filesystem = buildTestFileSystem();
//    System.out.println(filesystem._root.print());
//    filesystem.copyFile(filesystem.getRootDirectory(), "~/documents/secrets/secrets.txt", "~/documents/newSecrets.txt");
//
//    System.out.println(filesystem._root.print());
//  }

  @Test
  public void testCreateFile() {
    Filesystem filesystem = buildTestFileSystem();

    filesystem.createEmptyFile(filesystem._root, "~/new_folder/with_sub_folders/andANewCamelCasedFile.txt", true);

    System.out.println(filesystem._root.print());
  }

  @Test
  public void testCreateFileFromDiffDirectoryAndAbsolutePath() {
    Filesystem filesystem = buildTestFileSystem();

    filesystem.createEmptyFile(filesystem._root.findDirectory("documents").get(), "~/new_folder/with_sub_folders/andANewCamelCasedFile.txt", true);

    System.out.println(filesystem._root);
  }

  @Test
  public void testCreateFileFromDiffDirectoryRelativePath() {
    Filesystem filesystem = buildTestFileSystem();

    filesystem.createEmptyFile(filesystem._root.findDirectory("documents").get(), "new_folder/with_sub_folders/andANewCamelCasedFile.txt", true);

    System.out.println(filesystem._root);

    filesystem.deleteFile(filesystem._root.findDirectory("documents").get(), "new_folder", true);

    System.out.println(filesystem._root);
  }

  private Filesystem buildTestFileSystem() {
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
