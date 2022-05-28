package com.bramdekker.main.resources;

import com.bramdekker.main.util.HaskellFileFilter;
import com.bramdekker.main.util.SubdirectoryFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A singleton class that contains functionality to generate, get and set a list of File objects
 * containing all Haskell files in the project.
 */
public class FileList {
  private static FileList instance;
  private List<File> haskellFiles = Collections.emptyList();
  private static String pathname;

  /** Private constructor to make it singleton. */
  private FileList() {}

  /**
   * Generates an instance of FileList by setting the haskellFiles and returning a class instance.
   *
   * @return instance of FileList with the haskellFiles populated.
   */
  private static FileList generateInstance() {
    FileList newFileList = new FileList();

    // If the pathname is a Haskell file, set this file as the file list.
    if (pathname.endsWith(".hs")) {
      newFileList.setHaskellFiles(List.of(new File(pathname)));
      return newFileList;
    }

    newFileList.setHaskellFiles(newFileList.getHaskellFilesFromDir(pathname));

    return newFileList;
  }

  /**
   * Gets all Haskell files from the project root directory and puts them in a List.
   *
   * @param directory the directory of the Haskell project.
   * @return all Haskell files in the directory as a List of File objects.
   */
  private List<File> getHaskellFilesFromDir(String directory) {
    List<File> files = new ArrayList<>();

    if (directory != null) {
      File dir = new File(directory);

      getAllFiles(files, dir);
    }

    return files;
  }

  /**
   * For a certain directory, adds all Haskell files in it to the files List.
   *
   * @param files a List with File objects.
   * @param dir a File object representing a directory.
   */
  private void getAllFiles(List<File> files, File dir) {
    File[] haskellFiles = dir.listFiles(new HaskellFileFilter());
    if (haskellFiles == null) {
      return;
    }
    files.addAll(Arrays.stream(haskellFiles).toList());

    File[] subdirectories = dir.listFiles(new SubdirectoryFilter());
    if (subdirectories == null) {
      return;
    }
    for (File subDir : subdirectories) {
      getAllFiles(files, subDir);
    }
  }

  /**
   * Initializes the static variable directory.
   *
   * @param newPathname the value with which the directory variable is initialized.
   */
  public static void init(String newPathname) {
    pathname = newPathname;
  }

  /**
   * Getter for the static variable instance.
   *
   * @return the singleton FileList instance.
   */
  public static FileList getInstance() {
    if (instance != null) {
      return instance;
    }

    instance = generateInstance();
    return instance;
  }

  /**
   * Getter for the haskellFiles variable.
   *
   * @return a List of File objects containing Haskell files.
   */
  public List<File> getHaskellFiles() {
    return this.haskellFiles;
  }

  /**
   * Setter for the haskellFiles variable.
   *
   * @param haskellFiles a List of File objects that is the new value for haskellFiles.
   */
  private void setHaskellFiles(List<File> haskellFiles) {
    this.haskellFiles = haskellFiles;
  }
}
