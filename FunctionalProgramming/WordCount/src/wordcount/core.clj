;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns wordcount.core
  (:require [wordcount.pages :refer :all]
            [wordcount.words :refer :all]))

(defn count-words-sequential [pages]
  (frequencies (mapcat get-words pages)))

(defn count-words-parallel [pages]
  (reduce (partial merge-with +)
    (pmap #(frequencies (get-words %)) pages)))

(defn count-words [pages]
  (reduce (partial merge-with +)
    (pmap count-words-sequential (partition-all 100 pages))))

(defn -main [& args]
  (time (count-words (take 100000 (get-pages "enwiki.xml"))))
  (shutdown-agents))
