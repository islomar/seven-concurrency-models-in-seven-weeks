;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns philosophers.util)

(defn swap-when!
  "If (pred current-value-of-atom) is true, atomically swaps the value
  of the atom to become (apply f current-value-of-atom args). Note that
  both pred and f may be called multiple times, and thus should be free
  of side-effects. Returns the value that was swapped in if the
  predicate was true, nil otherwise."
  [a pred f & args]
  (loop [] 
    (let [old @a]
      (if (pred old)
        (let [new (apply f old args)] 
          (if (compare-and-set! a old new) 
            new
            (recur))) 
        nil))))
