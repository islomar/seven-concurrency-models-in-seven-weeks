;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns test.core
  (:require [test.jabberwocky :refer :all]
            [clj-http.client  :as client]))

(future
  (doseq [n (iterate inc 0)]
    (println (:body (client/get (str "http://localhost:3000/translation/" n))))))

(defn -main [& args]
  (doseq [n (range (count jabberwocky))]
    (client/put (str "http://localhost:3000/snippet/" n) {:body (nth jabberwocky n)})))