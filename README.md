# lein4clojure

A Leiningen plugin that creates clojure unit tests for problems from www.4clojure.com

## Usage

Use this for user-level plugins:

Put `[com.yuriy-zubarev/lein4clojure "0.9.0-SNAPSHOT"]` into the `:plugins` vector of your
`:user` profile.

Use this for project-level plugins:

Put `[com.yuriy-zubarev/lein4clojure "0.9.0-SNAPSHOT"]` into the `:plugins` vector of your project.clj.

See `sample-project` for an example.

    $ lein lein4clojure
    
    Test:  ./my-project/test/lein4clojure/elementary/001_nothing_but_the_truth.clj ... creating
	Test:  ./my-project/test/lein4clojure/elementary/002_simple_math.clj ... creating
	Test:  ./my-project/test/lein4clojure/elementary/003_intro_to_strings.clj ... creating
	Test:  ./my-project/test/lein4clojure/elementary/004_intro_to_lists.clj ... creating
	Test:  ./my-project/test/lein4clojure/elementary/005_lists__conj.clj ... creating
	Test:  ./my-project/test/lein4clojure/elementary/006_intro_to_vectors.clj ... creating
	Test:  ./my-project/test/lein4clojure/elementary/007_vectors__conj.clj ... creating
	...

The project is my personal Clojure playground and it's in a pretty rough shape as I'm just embarking on the wonderful world of Clojure.

## License

Copyright © 2012 Yuriy Zubarev

Distributed under the Eclipse Public License, the same as Clojure.
