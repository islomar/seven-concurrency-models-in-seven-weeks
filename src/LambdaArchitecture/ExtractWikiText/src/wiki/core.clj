;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns wiki.core
  (:require [wiki.pages :refer :all]))

(defn -main [& args]
  (try
    (let [[infile outfile page-count] args
          pages (get-pages infile)
          trimmed-pages (if page-count (take (Integer. page-count) pages) pages)]
      (doseq [page trimmed-pages]
        (spit outfile page :append true)))
    (catch Exception _ (println "Usage: <infile> <outfile> <page-count?>"))))
