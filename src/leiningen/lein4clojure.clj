(ns leiningen.lein4clojure
	(:require 
        [clojure.data.json :as json]
        [clojure.string :as str]
        [clostache.parser :as clostache]))

(import '(java.io File))

(defn- fetch-exercise [n]
	(json/read-json (slurp (str n ".json"))))
	; (println (slurp (str "http://4clojure.com/api/problem/" n))))

(defn- substitute-with [s subst]
  (str/replace (str/lower-case s) #"[ :]" subst))

(defn- excercise-name [jdoc]
  (substitute-with (:title jdoc) "-"))

(defn- excercise-ns [n jdoc]
  (str (format "%03d" n) "_" (substitute-with (:title jdoc) "_")))

(defn- excercise-file-name [n jdoc]
	(str "test/leiningen/" (excercise-ns n jdoc) ".clj"))

(defn- exercise-exist? [fname]
	(let [f (File. fname)]
		(.exists f)))

(defn- create-exercise [fname jdoc]
	(spit fname (clostache/render-resource "resources/hello.mustache" jdoc)))

(defn- reformat [s fname]
  (str/replace s "__" (str "(" fname ")")))

(defn process-exercise [n]
  (do 
    (let [
        jdoc (fetch-exercise n)
        test-name (excercise-name jdoc)
        test-file-name (excercise-file-name n jdoc)
        test-file-ns (excercise-ns n jdoc)
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

(defn lein4clojure
  "I don't do a lot."
  [project & args]
  (doseq [n (range 1 10)]
    (process-exercise n)))


