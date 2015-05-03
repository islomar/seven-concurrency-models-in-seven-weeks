# Seven concurrency models in seven weeks
Repository for the example code of the book ["Seven concurrency models in seven weeks"](https://pragprog.com/book/pb7con/seven-concurrency-models-in-seven-weeks).

##Discussions
https://forums.pragprog.com/forums/291

##Errata
https://pragprog.com/titles/pb7con/errata

##Chapter 1
A **concurrent program** has multiple logical threads of control. These threads
may or may not run in parallel.
A **parallel program** potentially runs more quickly than a sequential program
by executing different parts of the computation simultaneously (in parallel).
It may or may not have more than one logical thread of control.

* Concurrency is an aspect of the problem domain—your program needs to handle multiple simultaneous (or near-simultaneous) events. 
* Parallelism, by contrast, is an aspect of the solution domain—you want to make your program faster by processing different portions of the problem in parallel.

* **Concurrency** is about dealing with lots of things at once. **Parallelism** is about doing lots of things at once.

#Interesting links
##Chapter 1
* Concurrency is not parallelism (it's better): http://concur.rspace.googlecode.com/hg/talk/concur.html#title-slide
