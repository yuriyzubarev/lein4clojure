(ns leiningen.lein4clojure
	(:require 
        [clojure.data.json :as json]
        [clojure.string :as str]
        [clostache.parser :as clostache]))

(import '(java.io File))

(defn take-while-3 
  ([pred coll] (take-while-3 pred coll 0))
  ([pred coll false-count]
    (lazy-seq
      (when-let [s (seq coll)]
        (cond
          (pred (first s)) (cons (first s) (take-while-3 pred (rest s)))
          (< false-count 3) (cons (first s) (take-while-3 pred (rest s) (inc false-count))))))))

(defn scontains? [^String big ^String little]
    (not (neg? (.indexOf big little))))

(defn fetch-problem [n]
	;(json/read-json (slurp (str n ".json"))))
	(json/read-json (slurp (str "http://www.4clojure.com/api/problem/" n))))

(defn- substitute-with [s subst]
  (str/replace (str/lower-case s) #"[ :>,'-]" subst))

(defn- create-test-dir [test-path]
  (let [test-dir (str test-path "/" "lein4clojure" "/")
        f (File. test-dir)]
    (.mkdir f)
    test-dir))

(defn- exercise-exist? [fname]
	(let [f (File. fname)]
		(.exists f)))

(defn- create-exercise [fname jdoc]
	(spit fname (clostache/render-resource "resources/test.mustache" jdoc)))

(defn- reformat [s fname]
  (str (str/replace s "__" (str "(" fname ")")) (if (scontains? s ";") "\n")))

(defn process-exercise [n params]
  (try
    (let [
        jdoc (fetch-problem n)
        test-dir (create-test-dir (:test-path params))
        test-name (substitute-with (:title jdoc) "-")
        test-file-ns (str (format "%03d" n) "_" (substitute-with test-name "_"))
        test-full-file-name (str test-dir test-file-ns ".clj")
        reformat-with-name #(reformat % test-name)]
      (println "Original: " jdoc)
      (if (exercise-exist? test-full-file-name)
        (do
          (println "Exist")
          true)
        (do
          (println "Doesn't exist")
          (let [jdoc-enriched (assoc jdoc 
              :l4c-description-lines (vec (str/split (:description jdoc) #"\n"))
              :l4c-ns test-file-ns 
              :l4c-tests (map reformat-with-name (:tests jdoc))
              :l4c-test-name test-name)]
            (println "Enriched: " jdoc-enriched)
            (create-exercise test-full-file-name jdoc-enriched))
          true)))
    (catch Exception e false)))

(defn lein4clojure
  "Seed the current project with tests from 4clojure.com"
  [project & args]
  (doall (take-while-3 true? (map #(process-exercise % { :test-path (first (:test-paths project)) }) (iterate inc 1)))))

