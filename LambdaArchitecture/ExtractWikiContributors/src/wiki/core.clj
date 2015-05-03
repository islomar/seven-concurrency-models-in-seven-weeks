;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns wiki.core
  (:require [wiki.contributors :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn -main [& args]
  (try
    (let [[infile outfile] args]
      (with-open [out (io/writer outfile)]
        (doseq [contribution (get-contributions infile)]
          (.write out (string/join " " contribution))
          (.write out "\n"))))
    (catch Exception _ (println "Usage: <infile> <outfile>"))))
