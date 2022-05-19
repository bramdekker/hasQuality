package com.bramdekker.main.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filename filter for subdirectories.
 *
 * @see java.io.FilenameFilter
 */
public class SubdirectoryFilter implements FilenameFilter {
  @Override
  public boolean accept(File file, String s) {
    return file.isDirectory();
  }
}
