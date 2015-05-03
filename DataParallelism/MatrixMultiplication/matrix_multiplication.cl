//---
// Excerpted from "Seven Concurrency Models in Seven Weeks",
// published by The Pragmatic Bookshelf.
// Copyrights apply to this code. It may not be used to create training material, 
// courses, books, articles, and the like. Contact us if you are in doubt.
// We make no guarantees that this code is fit for any purpose. 
// Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
//---
__kernel void matrix_multiplication(uint widthA,
                                    __global const float* inputA,
                                    __global const float* inputB,
                                    __global float* output) {

  int i = get_global_id(0); 
  int j = get_global_id(1); 

  // Note that:
  // outputWidth == widthB
  // outputHeight == heightA
  // widthA == heightB
  int outputWidth = get_global_size(0); 
  int outputHeight = get_global_size(1); 
  int widthB = outputWidth;

  float total = 0.0;
  for (int k = 0; k < widthA; ++k) { 
    total += inputA[j * widthA + k] * inputB[k * widthB + i];
  }
  output[j * outputWidth + i] = total;
}
