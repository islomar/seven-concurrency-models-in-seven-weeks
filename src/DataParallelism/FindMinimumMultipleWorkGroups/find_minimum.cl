//---
// Excerpted from "Seven Concurrency Models in Seven Weeks",
// published by The Pragmatic Bookshelf.
// Copyrights apply to this code. It may not be used to create training material, 
// courses, books, articles, and the like. Contact us if you are in doubt.
// We make no guarantees that this code is fit for any purpose. 
// Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
//---
__kernel void find_minimum(__global const float* values,
                           __global float* results,
                           __local float* scratch) {

  int i = get_local_id(0);
  int n = get_local_size(0);

  scratch[i] = values[get_global_id(0)];

  barrier(CLK_LOCAL_MEM_FENCE);

  for (int j = n / 2; j > 0; j /= 2) {
    if (i < j)
      scratch[i] = min(scratch[i], scratch[i + j]);
    barrier(CLK_LOCAL_MEM_FENCE);
  }

  if (i == 0)
    results[get_group_id(0)] = scratch[0];
}