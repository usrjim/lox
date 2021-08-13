(ns lox.core
  (:require [clojure.java.io :as io])
  (:gen-class))

(def had-error (atom false))

(defn report [line where message]
  (println (format "[line %d] Error %s: %s" line where message))
  (reset! had-error true))

(defn error [line message]
  (report line "" message))

(defn run [source]
  (if @had-error
    (println "error, exit here")
    (println (format "pass %s to scanner" source))))

(defn run-file [path]
  (-> path slurp run))

(defn -main [& args]
  (println "Usage: clox [script]"))
