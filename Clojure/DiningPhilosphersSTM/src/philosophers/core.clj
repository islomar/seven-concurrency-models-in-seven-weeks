;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns philosophers.core)

(def philosophers (into [] (repeatedly 5 #(ref :thinking))))

(defn claim-chopsticks [philosopher left right]
  (dosync
    (when (and (= (ensure left) :thinking) (= (ensure right) :thinking))
      (ref-set philosopher :eating))))

(defn release-chopsticks [philosopher]
  (dosync (ref-set philosopher :thinking)))

(defn think []
  (Thread/sleep (rand 1000)))

(defn eat []
  (Thread/sleep (rand 1000)))

(defn philosopher-thread [n]
  (Thread.
    #(let [philosopher (philosophers n)
           left (philosophers (mod (- n 1) 5))
           right (philosophers (mod (+ n 1) 5))]
      (while true 
        (think)
        (when (claim-chopsticks philosopher left right) 
          (eat)
          (release-chopsticks philosopher)))))) 

(defn -main [& args]
  (let [threads (map philosopher-thread (range 5))]
    (doseq [thread threads] (.start thread))
    (doseq [thread threads] (.join thread))))
