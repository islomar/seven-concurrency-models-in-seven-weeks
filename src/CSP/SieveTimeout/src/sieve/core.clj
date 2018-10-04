;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns sieve.core
  (:require [clojure.core.async :as async :refer :all :exclude [map into reduce merge partition partition-by take]]))

(defn factor? [x y]
  (zero? (mod y x)))

(defn get-primes []
  (let [primes (chan)
        numbers (to-chan (iterate inc 2))]
    (go-loop [ch numbers]
      (when-let [prime (<! ch)]
        (>! primes prime)
        (recur (remove< (partial factor? prime) ch)))
      (close! primes))
    primes))

(defn -main [seconds]
  (let [primes (get-primes)
        limit (timeout (* (Integer. seconds) 1000))]
    (loop []
      (alt!! :priority true
        limit nil
        primes ([prime] (println prime) (recur))))))
