/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.io.*;
import java.net.URL;
import java.util.concurrent.CopyOnWriteArrayList;

class Downloader extends Thread {
  private InputStream in;
  private OutputStream out;
  private CopyOnWriteArrayList<ProgressListener> listeners;

  public Downloader(URL url, String outputFilename) throws IOException {
    in = url.openConnection().getInputStream();
    out = new FileOutputStream(outputFilename);
    listeners = new CopyOnWriteArrayList<ProgressListener>();
  }

  public void addListener(ProgressListener listener) {
    listeners.add(listener);
  }

  public void removeListener(ProgressListener listener) {
    listeners.remove(listener);
  }

  private void updateProgress(int n) {
    for (ProgressListener listener: listeners)
      listener.onProgress(n);
  }

  public void run() {
    int n = 0, total = 0;
    byte[] buffer = new byte[1024];

    try {
      while((n = in.read(buffer)) != -1) {
        out.write(buffer, 0, n);
        total += n;
        updateProgress(total);
      }
      out.flush();
    } catch (IOException e) { }
  }
}
