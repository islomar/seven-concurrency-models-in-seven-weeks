/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.util.List;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.input.Mouse;

import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CLPlatform;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLContext;
import org.lwjgl.opencl.CLCommandQueue;
import org.lwjgl.opencl.CLMem;
import org.lwjgl.opencl.CLProgram;
import org.lwjgl.opencl.CLKernel;
import org.lwjgl.opencl.Util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.opencl.CL10GL.*;

public class Zoom {

  private void start() {
    try {
      Display.setDisplayMode(new DisplayMode(600,600));
      Display.create();
      Display.setTitle("Zoom");
      Drawable drawable = Display.getDrawable();

      float planeDistance = (float)(1.0 / -Math.tan(Math.PI / 8));

      glMatrixMode(GL_PROJECTION);
      glLoadIdentity();
      GLU.gluPerspective(45.0f, 1.0f, 1.0f, 10.0f);
      glMatrixMode(GL_MODELVIEW);
      glPolygonMode(GL_FRONT, GL_LINE);
      glFrontFace(GL_CW);
      glEnable(GL_CULL_FACE);
      glCullFace(GL_BACK);
      glEnableClientState(GL_VERTEX_ARRAY);

      Mesh mesh = new Mesh(2.0f, 2.0f, 64, 64);

      int vertexBuffer = glGenBuffers();
      glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
      glBufferData(GL_ARRAY_BUFFER, mesh.vertices, GL_DYNAMIC_DRAW);

      int indexBuffer = glGenBuffers();
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, mesh.indices, GL_STATIC_DRAW);

      glVertexPointer(3, GL_FLOAT, 0, 0);

      CL.create();
      CLPlatform platform = CLPlatform.getPlatforms().get(0);
      List<CLDevice> devices = platform.getDevices(CL_DEVICE_TYPE_GPU);
      CLContext context = CLContext.create(platform, devices, null, drawable, null);
      CLCommandQueue queue = clCreateCommandQueue(context, devices.get(0), 0, null);

      CLProgram program =
        clCreateProgramWithSource(context, loadSource("zoom.cl"), null);
      Util.checkCLError(clBuildProgram(program, devices.get(0), "", null));
      CLKernel kernel = clCreateKernel(program, "zoom", null);

      PointerBuffer workSize = BufferUtils.createPointerBuffer(1);
      workSize.put(0, mesh.vertexCount * 3);

      CLMem vertexBufferCL = 
        clCreateFromGLBuffer(context, CL_MEM_READ_WRITE, vertexBuffer, null);

      while (!Display.isCloseRequested()) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);  
        glLoadIdentity();
        glTranslatef(0.0f, 0.0f, planeDistance);
        glDrawElements(GL_TRIANGLE_STRIP, mesh.indexCount, GL_UNSIGNED_SHORT, 0);
   
        Display.update();

        Util.checkCLError(clEnqueueAcquireGLObjects(queue, vertexBufferCL, null, null));
        kernel.setArg(0, vertexBufferCL);
        clEnqueueNDRangeKernel(queue, kernel, 1, null, workSize, null, null, null);
        Util.checkCLError(clEnqueueReleaseGLObjects(queue, vertexBufferCL, null, null));
        clFinish(queue);
      }

      clReleaseMemObject(vertexBufferCL);
      clReleaseKernel(kernel);
      clReleaseProgram(program);
      clReleaseCommandQueue(queue);
      clReleaseContext(context);
      CL.destroy();

      Display.destroy();

    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  private static String loadSource(String name) throws Exception {
    BufferedReader reader = null;
    try {
      File sourceFile = new File(Zoom.class.getClassLoader().getResource(name).toURI());
      reader = new BufferedReader(new FileReader(sourceFile));
      String line = null;
      StringBuilder result = new StringBuilder();
      while((line = reader.readLine()) != null) {
        result.append(line);
        result.append("\n");
      }
      return result.toString();
    } finally {
      reader.close();
    }
  }

  public static void main(String[] argv) {
    Zoom zoom = new Zoom();
    zoom.start();
  }
}