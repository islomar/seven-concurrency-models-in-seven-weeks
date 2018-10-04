ifeq ($(shell uname -s),Darwin)
	LIBS=-framework OpenCL
else
	LIBS=-lOpenCL
endif

target/multiply_arrays: multiply_arrays.c
	mkdir -p target
	gcc -std=c99 multiply_arrays.c $(LIBS) -o target/multiply_arrays
