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
#include <float.h>

#define WIDTH_A 200
#define HEIGHT_A 400
#define ELEMENTS_A (WIDTH_A * HEIGHT_A)

#define WIDTH_B 300
#define HEIGHT_B 200
#define ELEMENTS_B (WIDTH_B * HEIGHT_B)

#define WIDTH_OUTPUT WIDTH_B
#define HEIGHT_OUTPUT HEIGHT_A
#define ELEMENTS_OUTPUT (WIDTH_OUTPUT * HEIGHT_OUTPUT)

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

  cl_platform_id platform;
  CHECK_STATUS(clGetPlatformIDs(1, &platform, NULL));

  cl_device_id device;
  CHECK_STATUS(clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, 1, &device, NULL));

  cl_context context = clCreateContext(NULL, 1, &device, NULL, NULL, &status);
  CHECK_STATUS(status);

  cl_command_queue queue = clCreateCommandQueue(context, device, 0, &status);
  CHECK_STATUS(status);

  char* source = read_source("matrix_multiplication.cl");
  cl_program program = clCreateProgramWithSource(context, 1,
    (const char**)&source, NULL, NULL);
  CHECK_STATUS(status);
  free(source);

  CHECK_STATUS(clBuildProgram(program, 0, NULL, NULL, NULL, NULL));

  cl_kernel kernel = clCreateKernel(program, "matrix_multiplication", &status);
  CHECK_STATUS(status);

  cl_float a[ELEMENTS_A], b[ELEMENTS_B];
  random_fill(a, ELEMENTS_A);
  random_fill(b, ELEMENTS_B);

  cl_mem inputA = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
    sizeof(cl_float) * ELEMENTS_A, a, &status);
  CHECK_STATUS(status);
  cl_mem inputB = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR,
    sizeof(cl_float) * ELEMENTS_B, b, &status);
  CHECK_STATUS(status);
  cl_mem output = clCreateBuffer(context, CL_MEM_WRITE_ONLY,
    sizeof(cl_float) * ELEMENTS_OUTPUT, NULL, &status);
  CHECK_STATUS(status);

  cl_int widthA = WIDTH_A;
  CHECK_STATUS(clSetKernelArg(kernel, 0, sizeof(cl_int), &widthA));
  CHECK_STATUS(clSetKernelArg(kernel, 1, sizeof(cl_mem), &inputA));
  CHECK_STATUS(clSetKernelArg(kernel, 2, sizeof(cl_mem), &inputB));
  CHECK_STATUS(clSetKernelArg(kernel, 3, sizeof(cl_mem), &output));

  size_t work_units[] = {WIDTH_OUTPUT, HEIGHT_OUTPUT};
  CHECK_STATUS(clEnqueueNDRangeKernel(queue, kernel, 2, NULL, work_units, 
    NULL, 0, NULL, NULL));

  cl_float results[ELEMENTS_OUTPUT];
  CHECK_STATUS(clEnqueueReadBuffer(queue, output, CL_TRUE, 0, sizeof(cl_float) * ELEMENTS_OUTPUT,
    results, 0, NULL, NULL));

  CHECK_STATUS(clReleaseMemObject(inputA));
  CHECK_STATUS(clReleaseMemObject(inputB));
  CHECK_STATUS(clReleaseMemObject(output));
  CHECK_STATUS(clReleaseKernel(kernel));
  CHECK_STATUS(clReleaseProgram(program));
  CHECK_STATUS(clReleaseCommandQueue(queue));
  CHECK_STATUS(clReleaseContext(context));

  return 0;
}
