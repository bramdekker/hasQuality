package com.bramdekker.main.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filename filter for Haskell files.
 *
 * @see java.io.FilenameFilter
 */
public class HaskellFileFilter implements FilenameFilter {
  @Override
  public boolean accept(File file, String s) {
    return s.endsWith(".hs") || s.endsWith(".lhs");
  }
}
