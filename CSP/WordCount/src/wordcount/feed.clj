;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns wordcount.feed
  (:require [clojure.core.async :as async :refer :all :exclude [map into reduce merge partition partition-by take]]
            [clojure.java.io :as io]
            [wordcount.polling :refer [poll]]
            [wordcount.http :refer [http-get]])
  (:import [com.sun.syndication.io XmlReader SyndFeedInput]))

(def poll-interval 60)

(defn get-links [feed]
  (map #(.getLink %) (.getEntries feed)))

(defn parse-feed [body]
  (let [reader (XmlReader. (io/input-stream (.getBytes body)))]
    (.build (SyndFeedInput.) reader)))

; Simple-minded feed polling function
; WARNING: Don't use in production (use conditional get instead)
(defn poll-feed [url]
  (let [ch (chan)]
    (poll poll-interval
      (when-let [response (<! (http-get url))]
        (let [feed (parse-feed (:body response))]
          (onto-chan ch (get-links feed) false))))
    ch))

(defn new-links [url]
  (let [in (poll-feed url)
        out (chan)]
    (go-loop [links #{}]
      (let [link (<! in)]
        (if (contains? links link)
          (recur links)
          (do
            (>! out link)
            (recur (conj links link))))))
    out))
