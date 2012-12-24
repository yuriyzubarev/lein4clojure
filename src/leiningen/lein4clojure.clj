(ns leiningen.lein4clojure
	(:require 
        [clojure.data.json :as json]
        [clojure.string :as str]
        [clostache.parser :as clostache]))

(import '(java.io File))

(defn- fetch-exercise [n]
	(json/read-json (slurp (str n ".json"))))
	; (println (slurp (str "http://4clojure.com/api/problem/" n))))

(defn- excercise-name [jdoc]
  (str/replace (str/lower-case (:title jdoc)) " " "-"))

(defn- excercise-ns [n jdoc]
  (str (format "%03d" n) "_" (str/replace (str/lower-case (:title jdoc)) " " "_")))

(defn- excercise-file-name [n jdoc]
	(str "test/leiningen/" (excercise-ns n jdoc) ".clj"))

(defn- exercise-exist? [fname]
	(let [f (File. fname)]
		(.exists f)))

(defn- create-exercise [fname jdoc]
	(spit fname (clostache/render-resource "resources/hello.mustache" jdoc)))

(defn- reformat [s fname]
  (str/replace s "__" (str "(" fname ")")))

(defn lein4clojure
  "I don't do a lot."
  [project & args]
  (do 
  	(println "Hi!") 
  	(let [
        jdoc (fetch-exercise 1)
        test-name (excercise-name jdoc)
        test-file-name (excercise-file-name 1 jdoc)
        test-file-ns (excercise-ns 1 jdoc)
        reformat-with-name #(reformat % test-name)]
	  	(println jdoc)
  		(if (exercise-exist? test-file-name)
  			(do
  				(println "Exist"))
  			(do
  				(println "Doesn't exist")
          (println (assoc jdoc :lein4clojure-ns test-file-ns))
  				(create-exercise 
  					test-file-name (assoc jdoc 
              :l4c-ns test-file-ns 
              :l4c-tests (map reformat-with-name (:tests jdoc))
              :l4c-test-name test-name)))))))


