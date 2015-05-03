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
            [wordcount.words :refer :all]
            [clojure.core.reducers :as r]
            [foldable-seq.core :refer [foldable-seq]]))

(defn frequencies-parallel [words]
  (r/fold (partial merge-with +)
          (fn [counts word] (assoc counts word (inc (get counts word 0))))
          words))

(defn count-words [pages]
  (frequencies-parallel (r/mapcat get-words (foldable-seq pages))))

(defn -main [& args]
  (count-words (get-pages 10000 "enwiki.xml"))
  nil)
