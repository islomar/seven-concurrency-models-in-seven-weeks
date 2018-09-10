;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns wiki.contributors
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]))

(defn- parent->children [parent tag]
  (filter #(= tag (:tag %)) (:content parent)))

(defn- parent->child [parent tag]
  (first (parent->children parent tag)))

(defn- content [tag]
  (first (:content tag)))

(defn- get-contribution [revision]
  (let [timestamp (content (parent->child revision :timestamp))
        id (content (parent->child revision :id))
        contributor (parent->child revision :contributor)
        username (content (parent->child contributor :username))
        contributor-id (content (parent->child contributor :id))]
    [timestamp id contributor-id username]))

(defn get-contributions [filename]
  (let [in (io/reader filename)
        pages (parent->children (xml/parse in) :page)
        revisions (mapcat #(parent->children % :revision) pages)]
    (map get-contribution revisions)))