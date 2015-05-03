;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns server.sentences
  (:require [clojure.string :refer [trim]]))

(defn sentence-split [text]
  (map trim (re-seq #"[^\.!\?:;]+[\.!\?:;]*" text)))

(defn is-sentence? [text]
  (re-matches #"^.*[\.!\?:;]$" text))

(defn sentence-join [x y]
  (if (is-sentence? x) y (str x " " y)))

(defn strings->sentences [strings]
  (filter is-sentence? 
    (reductions sentence-join 
      (mapcat sentence-split strings))))
