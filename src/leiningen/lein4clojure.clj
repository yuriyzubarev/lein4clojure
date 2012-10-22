(ns leiningen.lein4clojure
	(:use 
        [clojure.data.json]
        [clojure.string :as str]))

(import '(java.io File))

(defn- fetch-exercise [n]
	(read-json (slurp (str n ".json"))))
	; (println (slurp (str "http://4clojure.com/api/problem/" n))))

(defn- excercise-file-name [n jdoc]
	(str (format "%03d" n) "-" (replace (lower-case (:title jdoc)) " " "-") ".clj"))

(defn- exercise-exist? [fname]
	(let [f (File. fname)]
		(.exists f)))

(defn lein4clojure
  "I don't do a lot."
  [project & args]
  (do 
  	(println "Hi!") 
  	(println (fetch-exercise 1))
  	(println (excercise-file-name 1 (fetch-exercise 1)))
  	(println (exercise-exist? (excercise-file-name 1 (fetch-exercise 1))))))


