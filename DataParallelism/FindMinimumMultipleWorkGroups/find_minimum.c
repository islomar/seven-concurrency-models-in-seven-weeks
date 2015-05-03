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
#include <time.h>
#include <math.h>
#include <float.h>

#define NUM_VALUES (1024 * 100)
#define WORKGROUP_SIZE 512
#define NUM_WORKGROUPS (NUM_VALUES / WORKGROUP_SIZE)

#define CHECK_STATUS(s) do { \
    cl_int ss = (s); \
    if (ss != CL_SUCCESS) { \
      fprintf(stderr, "Error %d at line %d\n", ss, __LINE__); \
      exit(1); \
    } \
  } while (0)

char* read_source(const char* filename) {
  FILE* h = fopen(filename, "r");
  if (!h) {
    fprintf(stderr, "Unable to open file %s", filename);
    exit(1);
  }
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
  cl_int status;
  srand(time(NULL));

  cl_platform_id platform;
  CHECK_STATUS(clGetPlatformIDs(1, &platform, NULL));

  cl_device_id device;
  CHECK_STATUS(clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, 1, &device, NULL));

  cl_context context = clCreateContext(NULL, 1, &device, NULL, NULL, &status);
  CHECK_STATUS(status);

  cl_command_queue queue = clCreateCommandQueue(context, device, 0, &status);
  CHECK_STATUS(status);

  char* source = read_source("find_minimum.cl");
  cl_program program = clCreateProgramWithSource(context, 1,
    (const char**)&source, NULL, NULL);
  CHECK_STATUS(status);
  free(source);

  CHECK_STATUS(clBuildProgram(program, 0, NULL, NULL, NULL, NULL));

  cl_kernel kernel = clCreateKernel(program, "find_minimum", &status);
  CHECK_STATUS(status);

  cl_float values[NUM_VALUES];
  random_fill(values, NUM_VALUES);

  cl_mem valuesBuffer = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
    sizeof(cl_float) * NUM_VALUES, values, &status);
  CHECK_STATUS(status);
  cl_mem resultBuffer = clCreateBuffer(context, CL_MEM_WRITE_ONLY,
    sizeof(cl_float) * NUM_WORKGROUPS, NULL, &status);
  CHECK_STATUS(status);

  CHECK_STATUS(clSetKernelArg(kernel, 0, sizeof(cl_mem), &valuesBuffer));
  CHECK_STATUS(clSetKernelArg(kernel, 1, sizeof(cl_mem), &resultBuffer));
  CHECK_STATUS(clSetKernelArg(kernel, 2, sizeof(cl_float) * WORKGROUP_SIZE, NULL));

  size_t work_units[] = {NUM_VALUES};
  size_t workgroup_size[] = {WORKGROUP_SIZE};
  CHECK_STATUS(clEnqueueNDRangeKernel(queue, kernel, 1, NULL, work_units, 
    workgroup_size, 0, NULL, NULL));

  cl_float results[NUM_WORKGROUPS];
  CHECK_STATUS(clEnqueueReadBuffer(queue, resultBuffer, CL_TRUE, 0,
    sizeof(cl_float) * NUM_WORKGROUPS, results, 0, NULL, NULL));

  CHECK_STATUS(clReleaseMemObject(resultBuffer));
  CHECK_STATUS(clReleaseMemObject(valuesBuffer));
  CHECK_STATUS(clReleaseKernel(kernel));
  CHECK_STATUS(clReleaseProgram(program));
  CHECK_STATUS(clReleaseCommandQueue(queue));
  CHECK_STATUS(clReleaseContext(context));

  cl_float min_result = FLT_MAX;
  for (int i = 0; i < NUM_WORKGROUPS; ++i) 
    min_result = fmin(min_result, results[i]);

  cl_float acc = FLT_MAX;
  for (int i = 0; i < NUM_VALUES; ++i)
    acc = fmin(acc, values[i]);

  if (acc != min_result)
    fprintf(stderr, "Error: %f != %f", acc, min_result);

  return 0;
}
