(ns internal.lein4clojure-test
  (:use [clojure.test])
  (:require [leiningen.lein4clojure :as t]))

(deftest downloading-non-existing-problem
	(is (= "aa" (t/fetch-exercise 1000000))))

