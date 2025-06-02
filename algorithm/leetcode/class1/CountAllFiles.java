package leetcode.class1;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

/*
Count All Files in a Directory  
  Given the path to a directory, implement a function that recursively counts all files (including hidden files) in that directory and its subdirectories.
  Return the total number of files.

  A file is defined as a regular file (including hidden ones).
  A directory (even hidden) does not count as a file.
  Assume the input path is valid and accessible.

  求一个文件系统有多少个子文件
 */
public class CountAllFiles {

  public int getFileNumber(String folderPath) {
    File root = new File(folderPath);
    if (!root.exists() || !root.isDirectory()) {
      return 0;
    }

    int fileCount = 0;
    Queue<File> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
      File current = queue.poll();
      File[] children = current.listFiles();

      if (children == null)
        continue;

      for (File child : children) {
        if (child.isFile()) {
          fileCount++;
        } else if (child.isDirectory()) {
          queue.offer(child);
        }
      }
    }

    return fileCount;
  }

}
