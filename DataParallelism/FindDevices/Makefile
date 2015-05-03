ifeq ($(shell uname -s),Darwin)
	LIBS=-framework OpenCL
else
	LIBS=-lOpenCL
endif

target/find_devices: find_devices.c
	mkdir -p target
	gcc -std=c99 find_devices.c $(LIBS) -o target/find_devices
