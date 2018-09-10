//---
// Excerpted from "Seven Concurrency Models in Seven Weeks",
// published by The Pragmatic Bookshelf.
// Copyrights apply to this code. It may not be used to create training material, 
// courses, books, articles, and the like. Contact us if you are in doubt.
// We make no guarantees that this code is fit for any purpose. 
// Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
//---
__kernel void multiply_arrays(__global const float* inputA,
                              __global const float* inputB,
                              __global float* output) {
   
  int i = get_global_id(0);
  output[i] = inputA[i] * inputB[i];
}