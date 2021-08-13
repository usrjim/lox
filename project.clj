(defproject lox "1.0.0"
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot lox.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
