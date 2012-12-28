(defproject com.yuriy-zubarev/lein4clojure "0.9.0-SNAPSHOT"
  :description "Seed the current project with tests from 4clojure.com"
  :url "https://github.com/yuriyzubarev/lein4clojure"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"],
  				 [org.clojure/data.json "0.1.3"],
  				 [de.ubercode.clostache/clostache "1.3.0"]]
  :eval-in-leiningen true

  :repositories {"snapshots" "https://oss.sonatype.org/content/repositories/snapshots/"}
  
  :deploy-repositories {"releases" {:url "https://oss.sonatype.org/service/local/staging/deploy/maven2/" :creds :gpg}
                        "snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots/" :creds :gpg}}
  
  ;;maven central requirements
  :scm {:url "git@github.com:yuriyzubarev/lein4clojure.git"}
  :pom-addition [:developers [:developer
                              [:name "Yuriy Zubarev"]
                              [:url "http://yuriy-zubarev.com"]
                              [:timezone "-8"]]])