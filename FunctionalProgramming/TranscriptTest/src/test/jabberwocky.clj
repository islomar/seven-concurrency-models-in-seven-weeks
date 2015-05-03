;---
; Excerpted from "Seven Concurrency Models in Seven Weeks",
; published by The Pragmatic Bookshelf.
; Copyrights apply to this code. It may not be used to create training material, 
; courses, books, articles, and the like. Contact us if you are in doubt.
; We make no guarantees that this code is fit for any purpose. 
; Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
;---
(ns test.jabberwocky)

(def jabberwocky [
  "Twas brillig, and the slithy toves"
  "Did gyre and gimble in the wabe:"
  "All mimsy were the borogoves,"
  "And the mome raths outgrabe."

  "Beware the Jabberwock, my son!"
  "The jaws that bite, the claws that catch!"
  "Beware the Jubjub bird, and shun"
  "The frumious Bandersnatch!"

  "He took his vorpal sword in hand:"
  "Long time the manxome foe he sought;"
  "So rested he by the Tumtum tree,"
  "And stood awhile in thought."

  "And, as in uffish thought he stood,"
  "The Jabberwock, with eyes of flame,"
  "Came whiffling through the tulgey wood,"
  "And burbled as it came!"

  "One, two! One, two! And through and through"
  "The vorpal blade went snicker-snack!"
  "He left it dead, and with its head"
  "He went galumphing back."

  "And, has thou slain the Jabberwock?"
  "Come to my arms, my beamish boy!"
  "O frabjous day! Callooh! Callay!"
  "He chortled in his joy."

  "Twas brillig, and the slithy toves"
  "Did gyre and gimble in the wabe:"
  "All mimsy were the borogoves,"
  "And the mome raths outgrabe."

  ""])