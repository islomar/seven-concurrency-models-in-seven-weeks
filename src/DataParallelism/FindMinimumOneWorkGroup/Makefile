ifeq ($(shell uname -s),Darwin)
	LIBS=-framework OpenCL
else
	LIBS=-lOpenCL
endif

target/find_minimum: find_minimum.c
	mkdir -p target
	gcc -std=c99 find_minimum.c $(LIBS) -o target/find_minimum
