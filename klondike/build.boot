(require '[boot.core :refer :all]                           ; IntelliJ "integration"
         '[boot.task.built-in :refer :all])

(task-options!
  pom {:project     'solitare-hoplon-demo
       :version     "0.1"
       :description "solitare-hoplon-demo"
       :license     {"EPL" "(c) Ben @wasserspiegel"}})

(set-env!
  :dependencies '[[org.clojure/clojure       "1.8.0"            :scope "provided"]
                  [org.clojure/clojurescript "1.9.293"          :scope "compile"]
                  [org.clojure/core.async    "0.2.395"]

;;; database, server & logging ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

                  [pandeiro/boot-http        "0.7.1-SNAPSHOT"   :scope "test"]

;                  [ring                      "1.4.0"]
;                  [ring/ring-defaults        "0.2.1"]
;
;                  [compojure                 "1.5.1"]
;                  [mount                     "0.1.7"]
;                  [environ                   "1.1.0"]
;
;                  [ch.qos.logback/logback-classic "1.0.1"]
;                  [ring-logger-onelog        "0.7.7"]
;                  [org.clojure/tools.logging "0.3.1"]

;;; backend framework  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  [hoplon                    "6.0.0-alpha17"]
;                  [hoplon/castra             "3.0.0-alpha7"]
;                  [cheshire                  "5.5.0"
;                            :exclusions [com.fasterxml.jackson.core/jackson-core]]

;;; frontend ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  [cljsjs/markdown            "0.6.0-beta1-0"]

;;; boot  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  [boot/core                 "2.7.1"            :scope "provided"]
                  [onetom/boot-lein-generate "0.1.3"            :scope "test"]
                  [adzerk/boot-cljs          "1.7.228-2"        :scope "compile"]
                  [adzerk/boot-reload        "0.4.13"           :scope "compile"]

;;; cljs-repl  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  [adzerk/boot-cljs-repl     "0.3.3"] ;; latest release

;;; cljs repl ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  [binaryage/dirac           "0.8.7"            :scope "test"]
                  [binaryage/devtools        "0.8.3"            :scope "test"]
                  [powerlaces/boot-cljs-devtools "0.1.2"        :scope "test"]



                  ]
  :source-paths   #{"src"}
  :resource-paths #{"assets"}
  :asset-paths    #{"assets"}
)

;create project file for cursive
(require
   '[boot.lein])
(boot.lein/generate)

(require
  '[adzerk.boot-reload              :refer [reload]]
  '[hoplon.boot-hoplon              :refer [hoplon prerender]]
  '[adzerk.boot-cljs                :refer [cljs]]
  '[powerlaces.boot-cljs-devtools   :refer [cljs-devtools]]
  '[pandeiro.boot-http              :refer [serve]]

  )



(def devtools-config
  {:features-to-install           [:formatters :hints :async]
   :dont-detect-custom-formatters true})

(deftask dev []
  (comp
    (watch)
    (hoplon)
    (reload)
    (cljs-devtools)
    (cljs :compiler-options {:parallel-build  true
                             :external-config {:devtools/config devtools-config}})
    (serve :port 3000)))

(deftask prod
         []
         (comp
           (hoplon)
           (cljs :optimizations :advanced)
           (prerender)))


