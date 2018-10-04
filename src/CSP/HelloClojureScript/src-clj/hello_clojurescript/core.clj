;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns hello-clojurescript.core
  (:require [compojure.core :refer :all]
            [compojure.handler :refer [site]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [redirect]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defroutes app-routes
  (GET "/" [] (redirect "/index.html"))
  (resources "/")
  (not-found "Page not found"))

(defn -main [& args]
  (run-jetty (site app-routes) {:port 3000}))
