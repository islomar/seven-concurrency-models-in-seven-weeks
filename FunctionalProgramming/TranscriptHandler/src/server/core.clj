;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns server.core
  (:require [compojure.core     :refer :all]
            [compojure.handler  :refer [site]]
            [ring.util.response :refer [response]]
            [ring.adapter.jetty :refer [run-jetty]]))

(def snippets (repeatedly promise))

(future 
  (doseq [snippet (map deref snippets)]
    (println snippet)))

(defn accept-snippet [n text]
  (deliver (nth snippets n) text))

(defroutes app-routes
  (PUT "/snippet/:n" [n :as {:keys [body]}]
    (accept-snippet (Integer. n) (slurp body))
    (response "OK")))

(defn -main [& args]
  (run-jetty (site app-routes) {:port 3000}))
