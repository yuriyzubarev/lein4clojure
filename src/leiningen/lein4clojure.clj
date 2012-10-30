(ns leiningen.lein4clojure
	(:use 
        [clojure.data.json]
        [clojure.string :as str]
        [clostache.parser]))

(import '(java.io File))

(defn- fetch-exercise [n]
	(read-json (slurp (str n ".json"))))
	; (println (slurp (str "http://4clojure.com/api/problem/" n))))

(defn- excercise-ns [n jdoc]
  (str (format "%03d" n) "_" (replace (lower-case (:title jdoc)) " " "_")))

(defn- excercise-file-name [n jdoc]
	(str "test/leiningen/" (excercise-ns n jdoc) ".clj"))

(defn- exercise-exist? [fname]
	(let [f (File. fname)]
		(.exists f)))

(defn- create-exercise [fname jdoc]
	(spit fname (render-resource "resources/hello.mustache" jdoc)))

(defn lein4clojure
  "I don't do a lot."
  [project & args]
  (do 
  	(println "Hi!") 
  	(let [
        jdoc (fetch-exercise 1)
        test-file-name (excercise-file-name 1 jdoc)
        test-file-ns (excercise-ns 1 jdoc)]
	  	(println jdoc)
  		(if (exercise-exist? test-file-name)
  			(do
  				(println "Exist"))
  			(do
  				(println "Doesn't exist")
  				(create-exercise 
  					test-file-name (assoc jdoc :lein4clojure-ns test-file-ns)))))))


