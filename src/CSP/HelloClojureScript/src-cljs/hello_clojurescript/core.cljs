;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns hello-clojurescript.core
  (:require-macros [cljs.core.async.macros :refer [go]]) 
  (:require [goog.dom :refer [append createDom getElement]]
            [cljs.core.async :refer [<! timeout]])) 

(defn output [elem message] 
  (append elem message (createDom "br")))

(defn start []
  (let [content (getElement "content")]
    (go 
      (while true 
        (<! (timeout 1000)) 
        (output content "Hello from task 1"))) 
    (go 
      (while true 
        (<! (timeout 1500)) 
        (output content "Hello from task 2"))))) 

(set! (.-onload js/window) start) 
