(ns leiningen.lein4clojure
	(:use 
        [clojure.data.json]))

(defn lein4clojure
  "I don't do a lot."
  [project & args]
  (println "Hi!" (slurp "http://4clojure.com/api/problem/1")))

