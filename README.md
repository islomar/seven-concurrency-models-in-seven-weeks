# Seven concurrency models in seven weeks
Repository for the example code of the book ["Seven concurrency models in seven weeks"](https://pragprog.com/book/pb7con/seven-concurrency-models-in-seven-weeks).

## Discussions
https://forums.pragprog.com/forums/291

## Errata
https://pragprog.com/titles/pb7con/errata

## Chapter 1
* A **concurrent program** has multiple logical threads of control. These threads may or may not run in parallel.
* A **parallel program** potentially runs more quickly than a sequential program by executing different parts of the computation simultaneously (in parallel).
It may or may not have more than one logical thread of control.

* **Concurrency** is an aspect of the problem domain—your program needs to handle multiple simultaneous (or near-simultaneous) events.
* **Parallelism**, by contrast, is an aspect of the solution domain—you want to make your program faster by processing different portions of the problem in parallel.

* **Concurrency** is about dealing with lots of things at once. **Parallelism** is about doing lots of things at once.

* Concurrent programs are often nondeterministic —they will give different results depending on the precise timing of events. If you’re working on a genuinely concurrent problem, nondeterminism is natural and to be expected.
* Parallelism, by contrast, doesn’t necessarily imply nondeterminism

Although there’s a tendency to think that parallelism means multiple cores, modern computers are parallel on many different levels. The reason why individual cores have been able to get faster every year, until recently, is that they’ve been using all those extra transistors predicted by Moore’s law in parallel, both at the bit and at the instruction level.

### Levels of parallelism
* Bit-level: i.e. 16, 32, 64-bit architectures.
* Instruction-level
* Data parallelism
* Task-level

## Interesting links
### Chapter 1
* Concurrency is not parallelism (it's better): http://concur.rspace.googlecode.com/hg/talk/concur.html#title-slide
