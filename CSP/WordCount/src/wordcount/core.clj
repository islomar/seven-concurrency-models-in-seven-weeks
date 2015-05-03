;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns wordcount.core
  (:require [clojure.core.async :as async :refer :all :exclude [map into reduce merge partition partition-by take]]
            [clojure.java.io :as io]
            [wordcount.feed :refer [new-links]]
            [wordcount.http :refer [http-get]]
            [wordcount.words :refer [get-words]]))

(defn get-counts [urls]
  (let [counts (chan)]
    (go (while true
          (let [url (<! urls)]
            (when-let [response (<! (http-get url))]
              (let [c (count (get-words (:body response)))]
                (>! counts [url c]))))))
    counts))

(defn -main [feeds-file]
  (with-open [rdr (io/reader feeds-file)] 
    (let [feed-urls (line-seq rdr) 
          article-urls (map new-links feed-urls) 
          article-counts (map get-counts article-urls) 
          counts (async/merge article-counts)] 
      (while true 
        (println (<!! counts))))))
