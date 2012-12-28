(ns leiningen.lein4clojure
	(:require 
        [clojure.data.json :as json]
        [clojure.string :as str]
        [clostache.parser :as clostache]))

(import '(java.io File))

(defn- fetch-exercise [n]
	;(json/read-json (slurp (str n ".json"))))
	(json/read-json (slurp (str "http://www.4clojure.com/api/problem/" n))))

(defn- substitute-with [s subst]
  (str/replace (str/lower-case s) #"[ :]" subst))

(defn- excercise-name [jdoc]
  (substitute-with (:title jdoc) "-"))

(defn- excercise-ns [n jdoc]
  (str (format "%03d" n) "_" (substitute-with (:title jdoc) "_")))

(defn- create-test-dir [test-path]
  (let [test-dir (str test-path "/" "lein4clojure" "/")
        f (File. test-dir)]
    (.mkdir f)
    test-dir))

(defn- excercise-file-name [n jdoc test-dir]
	(str test-dir (excercise-ns n jdoc) ".clj"))

(defn- exercise-exist? [fname]
	(let [f (File. fname)]
		(.exists f)))

(defn- create-exercise [fname jdoc]
	(spit fname (clostache/render-resource "resources/test.mustache" jdoc)))

(defn- reformat [s fname]
  (str/replace s "__" (str "(" fname ")")))

(defn process-exercise [n params]
  (do 
    (let [
        jdoc (fetch-exercise n)
        test-dir (create-test-dir (:test-path params))
        test-name (excercise-name jdoc)
        test-file-name (excercise-file-name n jdoc test-dir)
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
  "Seed the current project with tests from 4clojure.com"
  [project & args]
  (doseq [n (range 1 11)]
    (process-exercise 
      n 
      { :test-path (first (:test-paths project)) })))
