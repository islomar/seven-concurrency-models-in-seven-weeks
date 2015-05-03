/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
#ifdef __APPLE__
#include <OpenCL/cl.h>
#else  
#include <CL/cl.h>
#endif

#include <stdio.h>
#include <mach/mach_time.h>
#include <inttypes.h>

#define NUM_ELEMENTS (1024 * 100)

char* read_source(const char* filename) {
  FILE *h = fopen(filename, "r");
  fseek(h, 0, SEEK_END);
  size_t s = ftell(h);
  rewind(h);
  char* program = (char*)malloc(s + 1);
  fread(program, sizeof(char), s, h);
  program[s] = '\0';
  fclose(h);
  return program;
}

void random_fill(cl_float array[], size_t size) {
  for (int i = 0; i < size; ++i)
    array[i] = (cl_float)rand() / RAND_MAX;
}

int main() {
  cl_platform_id platform;
  clGetPlatformIDs(1, &platform, NULL);

  cl_device_id device;
  clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, 1, &device, NULL);

  cl_context context = clCreateContext(NULL, 1, &device, NULL, NULL, NULL);

  cl_command_queue queue = clCreateCommandQueue(context, device, CL_QUEUE_PROFILING_ENABLE, NULL);

  char* source = read_source("multiply_arrays.cl");
  cl_program program = clCreateProgramWithSource(context, 1,
    (const char**)&source, NULL, NULL);
  free(source);

  clBuildProgram(program, 0, NULL, NULL, NULL, NULL);

  cl_kernel kernel = clCreateKernel(program, "multiply_arrays", NULL);

  cl_float a[NUM_ELEMENTS], b[NUM_ELEMENTS];
  random_fill(a, NUM_ELEMENTS);
  random_fill(b, NUM_ELEMENTS);

  uint64_t startGPU = mach_absolute_time();

  cl_mem inputA = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
    sizeof(cl_float) * NUM_ELEMENTS, a, NULL);
  cl_mem inputB = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
    sizeof(cl_float) * NUM_ELEMENTS, b, NULL);
  cl_mem output = clCreateBuffer(context, CL_MEM_WRITE_ONLY,
    sizeof(cl_float) * NUM_ELEMENTS, NULL, NULL);

  clSetKernelArg(kernel, 0, sizeof(cl_mem), &inputA);
  clSetKernelArg(kernel, 1, sizeof(cl_mem), &inputB);
  clSetKernelArg(kernel, 2, sizeof(cl_mem), &output);

  cl_event timing_event;
  size_t work_units = NUM_ELEMENTS;
  clEnqueueNDRangeKernel(queue, kernel, 1, NULL, &work_units, 
    NULL, 0, NULL, &timing_event);

  cl_float results[NUM_ELEMENTS];
  clEnqueueReadBuffer(queue, output, CL_TRUE, 0, sizeof(cl_float) * NUM_ELEMENTS,
    results, 0, NULL, NULL);

  uint64_t endGPU = mach_absolute_time();
  printf("Total (GPU): %lu ns\n\n", (unsigned long)(endGPU - startGPU));

  cl_ulong starttime;
  clGetEventProfilingInfo(timing_event, CL_PROFILING_COMMAND_START, 
    sizeof(cl_ulong), &starttime, NULL);
  cl_ulong endtime;
  clGetEventProfilingInfo(timing_event, CL_PROFILING_COMMAND_END, 
    sizeof(cl_ulong), &endtime, NULL);
  printf("Elapsed (GPU): %lu ns\n\n", (unsigned long)(endtime - starttime));

  clReleaseEvent(timing_event);
  clReleaseMemObject(inputA);
  clReleaseMemObject(inputB);
  clReleaseMemObject(output);
  clReleaseKernel(kernel);
  clReleaseProgram(program);
  clReleaseCommandQueue(queue);
  clReleaseContext(context);

  uint64_t startCPU = mach_absolute_time();

  for (int i = 0; i < NUM_ELEMENTS; ++i)
    results[i] = a[i] * b[i];

  uint64_t endCPU = mach_absolute_time();
  printf("Elapsed (CPU): %lu ns\n\n", (unsigned long)(endCPU - startCPU));

  return 0;
}
