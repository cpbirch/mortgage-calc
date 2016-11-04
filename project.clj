(defproject mortgage-calc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [aleph "0.4.2-alpha8"]
                 [bidi "2.0.12"]
                 [com.stuartsierra/component "0.3.1"]
                 [hiccup "1.0.5"]
                 [yada "1.1.37" :exclusions [aleph manifold ring-swagger prismatic/schema]]
                 ]
  :main ^:skip-aot mortgage-calc.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
