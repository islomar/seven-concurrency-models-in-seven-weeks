;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns wordcount.pages
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]))

(defn- parent->children [parent tag]
  (filter #(= tag (:tag %)) (:content parent)))

(defn- parent->child [parent tag]
  (first (parent->children parent tag)))

(defn- content [tag]
  (first (:content tag)))

(defn- text [page]
  (let [revision (parent->child page :revision)
        text (parent->child revision :text)]
    (content text)))

(defn get-pages [n filename]
  (let [in (io/reader filename)
        content (:content (xml/parse in))
        pages (take n (filter #(= :page (:tag %)) content))]
    (map text pages)))
