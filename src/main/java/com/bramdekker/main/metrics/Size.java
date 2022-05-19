package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;
import java.io.File;

// - lines of code (LOC)
//      -> noncommented lines (NCLOC)
//      - Blank lines -> not counted
//      - Comment lines -> counted separately (CLOC)
//      - Data declarations
//      - Lines that contain several separate instructions
// - number of executable statements (ES)
//      - comment lines, data declarations, and headings are ignored
//      - count separate statements on the same physical line as distinct
// - number of delivered source instructions (DSI)
//      - it counts separate statements on the same physical line as distinct
//      - ignores comment lines
// - number of characters (wc -m <filename>)
// - number in bytes
// - graph with modules/statements as nodes and control flow/data links as edges

/** Collection of methods that determine size metrics. */
public class Size {
  /**
   * Generate the section for size metrics.
   *
   * @return a report section containing information about size metrics as String.
   */
  public static String getSection() {
    StringBuilder sizeSection = new StringBuilder("Size metrics: \n");

    for (File file : FileList.getInstance().getHaskellFiles()) {
      sizeSection.append(file.toString());
      sizeSection.append("\n");
    }

    return sizeSection.toString();
  }
}
