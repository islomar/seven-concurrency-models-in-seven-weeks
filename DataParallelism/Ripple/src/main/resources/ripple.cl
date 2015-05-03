//---
// Excerpted from "Seven Concurrency Models in Seven Weeks",
// published by The Pragmatic Bookshelf.
// Copyrights apply to this code. It may not be used to create training material, 
// courses, books, articles, and the like. Contact us if you are in doubt.
// We make no guarantees that this code is fit for any purpose. 
// Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
//---
#define AMPLITUDE 0.1
#define FREQUENCY 10.0
#define SPEED 0.5
#define WAVE_PACKET 50.0
#define DECAY_RATE 2.0

__kernel void ripple(__global float* vertices,
                     __global float* centres,
                     __global float* times,
                     unsigned int num_centres,
                     float now) {

  unsigned int id = get_global_id(0);
  unsigned int offset = id * 3;

  float x = vertices[offset]; 
  float y = vertices[offset + 1]; 
  float z = 0.0;

  for (int i = 0; i < num_centres; ++i) { 
    if (times[i] != 0.0) {
      float dx = x - centres[i * 2]; 
      float dy = y - centres[i * 2 + 1]; 
      float d = sqrt(dx * dx + dy * dy); 

      float elapsed = now - times[i];
      float r = elapsed * SPEED; 

      float delta = r - d; 

      z += AMPLITUDE *
        exp(-DECAY_RATE * r * r) * 
        exp(-WAVE_PACKET * delta * delta) *
        cos(FREQUENCY * M_PI_F * delta);
    }
  } 

  vertices[offset + 2] = z; 
}
