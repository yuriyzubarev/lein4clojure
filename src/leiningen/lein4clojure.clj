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

(defn fetch-exception-set []
    (try
        (set (map #(Integer/parseInt %) (str/split (slurp "https://gist.github.com/raw/4562392/lein4clojure.txt") #",")))
        (catch Exception e #{})))

(defn- substitute-with [s subst]
    (str/replace (str/lower-case s) #"[ :>,'-]" subst))

(defn- create-test-dir [test-path difficulty]
    (let [  base-test-dir (str test-path "/lein4clojure/")
            test-dir (str base-test-dir difficulty "/")
            basef (File. base-test-dir)
            f (File. test-dir)]
        (.mkdir basef)
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
                test-difficulty (str/lower-case (:difficulty jdoc))
                test-dir (create-test-dir (:test-path params) test-difficulty)
                test-name (substitute-with (:title jdoc) "-")
                test-class-name  (str (format "%03d" n) "_" (substitute-with test-name "_"))
                test-file-ns (str test-difficulty "." test-class-name)
                test-full-file-name (str test-dir test-class-name ".clj")
                reformat-with-name #(reformat % test-name)]
            (print "Test: " test-full-file-name "... ")
            (if (exercise-exist? test-full-file-name)
                (do
                    (println "already exists")
                    true)
                (do
                    (println "creating")
                    (let [jdoc-enriched (assoc jdoc 
                            :l4c-description-lines (str/split (:description jdoc) #"\n")
                            :l4c-ns test-file-ns 
                            :l4c-tests (map reformat-with-name (:tests jdoc))
                            :l4c-tests-comment (contains? (:exception-set params) n)
                            :l4c-test-name test-name)]
                        ; (println "Enriched: " jdoc-enriched)
                        (create-exercise test-full-file-name jdoc-enriched))
                    true)))
        (catch Exception e 
            (binding [*out* *err*]
                (println "a little problem: " (.getMessage e)))
            false)))

(defn lein4clojure
    "Seed the current project with tests from www.4clojure.com"
    [project & args]
        (let [params { :test-path (first (:test-paths project)) :exception-set (fetch-exception-set) }]
            (doall (take-while-3 true? (map #(process-exercise % params) (iterate inc 1))))))
