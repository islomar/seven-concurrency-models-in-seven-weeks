ifeq ($(shell uname -s),Darwin)
	LIBS=-framework OpenCL
else
	LIBS=-lOpenCL
endif

target/matrix_multiplication: matrix_multiplication.c
	mkdir -p target
	gcc -std=c99 matrix_multiplication.c $(LIBS) -o target/matrix_multiplication
