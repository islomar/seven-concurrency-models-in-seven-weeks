/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;

class Mesh {

  FloatBuffer vertices;
  ShortBuffer indices;
  int indexCount;
  int vertexCount;

  public Mesh (float width, float height, int columns, int rows) {
    vertexCount = columns * rows;

    int ordinaryVertices = 2 * columns * (rows - 1);
    int degenerateVertices = (rows - 2) * 2;
    indexCount = ordinaryVertices + degenerateVertices;

    vertices = BufferUtils.createFloatBuffer(vertexCount * 3);
    indices = BufferUtils.createShortBuffer(indexCount);
    
    // Generate ordinaryVertices in the range [-width/2 ... 0 ... width/2]
    // and similarly for height
    for (int i = 0; i < rows; ++i) {
      float y = -height / 2 + height * (float)i / (float)(rows - 1);
      for(int j = 0; j < columns; ++j) {
        float x = -width / 2 + width * (float)j / (float)(columns - 1);
        float z = 0.0f;

        vertices.put(x);
        vertices.put(y);
        vertices.put(z);
      }
    }
    vertices.flip();

    // Indices for a single triangle strip. See:
    // http://marc.blog.atpurpose.com/2009/10/24/programatically-generating-a-rectangular-mesh-using-single-gl_triangle_strip/
    for (int row = 0; row < rows - 1; ++row) {
      for (int column = 0; column < columns; ++column) {
        indices.put((short)(row * columns + column));
        indices.put((short)((row + 1) * columns + column));
      }
      
      // Extra ordinaryVertices (of degenerate triangles) at the end of this row connecting it to the start of the next one
      if (row < rows - 2) {
        indices.put((short)((row + 1) * columns + (columns - 1)));
        indices.put((short)((row + 1) * columns));
      }
    }
    indices.flip();
  }
}
