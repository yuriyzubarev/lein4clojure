(ns internal.lein4clojure-test
  (:use [clojure.test])
  (:require [leiningen.lein4clojure :as t]))

;(deftest downloading-non-existing-problem
;	(is (thrown? java.io.FileNotFoundException (t/fetch-exercise 99999))))


; (defn function-with-side-effects [n]
;   (if (> n 10) (do (println n) false) (do (println n) true)))

; (defn call-function-with-side-effects []
;   (doall (take-while-3 true? (map function-with-side-effects (iterate inc 0)))))

; (deftest test-function-with-side-effects
; 	(call-function-with-side-effects))

