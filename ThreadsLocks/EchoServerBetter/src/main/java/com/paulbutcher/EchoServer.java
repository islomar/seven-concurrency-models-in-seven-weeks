/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class EchoServer {

  public static void main(String[] args) throws IOException {

    class ConnectionHandler implements Runnable {
      InputStream in; OutputStream out;

      ConnectionHandler(Socket socket) throws IOException {
        in = socket.getInputStream();
        out = socket.getOutputStream();
      }

      public void run() {
        try {
          int n;
          byte[] buffer = new byte[1024];
          while((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
            out.flush();
          }
        } catch (IOException e) {}
      }
    }

    ServerSocket server = new ServerSocket(4567);
    int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
    ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
    while (true) {
      Socket socket = server.accept();
      executor.execute(new ConnectionHandler(socket));
    }
  }
}
