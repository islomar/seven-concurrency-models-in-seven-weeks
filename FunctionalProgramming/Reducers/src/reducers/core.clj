;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns reducers.core
  (:require [clojure.core.protocols :refer [CollReduce coll-reduce]]
            [clojure.core.reducers :refer [CollFold coll-fold]]))

(defn my-reduce
  ([f coll] (coll-reduce coll f))
  ([f init coll] (coll-reduce coll f init)))

(defn my-fold
  ([reducef coll]
    (my-fold reducef reducef coll))
  ([combinef reducef coll]
    (my-fold 512 combinef reducef coll))
  ([n combinef reducef coll]
    (coll-fold coll n combinef reducef)))

(defn make-reducer [reducible transformf]
  (reify
    CollFold
    (coll-fold [_ n combinef reducef]
      (coll-fold reducible n combinef (transformf reducef)))

    CollReduce
    (coll-reduce [_ f1]
      (coll-reduce reducible (transformf f1) (f1)))
    (coll-reduce [_ f1 init]
      (coll-reduce reducible (transformf f1) init))))

(defn my-map [mapf reducible]
  (make-reducer reducible
    (fn [reducef]
      (fn [acc v]
        (reducef acc (mapf v))))))

(defn my-filter [filterf reducible]
  (make-reducer reducible
    (fn [reducef]
      (fn [acc v]
        (if (filterf v)
          (reducef acc v)
          acc)))))

(defn my-flatten [reducible]
  (make-reducer reducible
    (fn [reducef]
      (fn [acc v]
        (if (sequential? v)
          (coll-reduce (my-flatten v) reducef acc)
          (reducef acc v))))))
