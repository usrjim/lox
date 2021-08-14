(ns lox.token
  (:require [clojure.java.io :as io])
  (:gen-class))

(def *had-error (atom false))
(def *source (atom {}))
(def *tokens (atom []))

(def *start (atom 0))
(def *current (atom 0))
(def *line (atom 1))

;; token record
(defrecord Token [type lexeme literal line])

;; error helpers
(defn report [line where message]
  (println (format "[line %d] Error %s: %s" line where message))
  (reset! *had-error true))

(defn error [line message]
  (report line "" message))

(defn scanner [source]
  (reset! *source source))

;; utils
(defn- is-at-end []
  (>= @*current (count @*source)))

(defn- advance []
  (.charAt @*source (swap! *current inc)))

(defn- match [expected]
  (when (not (is-at-end))
    (if (= expected (.charAt @*source @*current))
      (do
        (swap! *current inc)
        true)
      false)))

(defn- add-token
  ([type]
   (add-token type nil))
  ([type literal]
   (let [text (.substring @*source @*start @*current)]
     (swap! *tokens conj (Token. type text literal @*line)))))

;; functions
(defn- scan-token []
  (let [c (advance)]
    (case c
      \( (add-token :LEFT_PAREN)
      \) (add-token :RIGHT_PAREN)
      \{ (add-token :LEFT_BRACE)
      \} (add-token :RIGHT_BRACE)
      \, (add-token :COMMA)
      \. (add-token :DOT)
      \- (add-token :MINUS)
      \+ (add-token :PLUS)
      \; (add-token :SEMICOLON)
      \* (add-token :STAR)
      \! (add-token (if (match \=) :BANG_EQUAL :BANG))
      \= (add-token (if (match \=) :EQUAL_EQUAL :EQUAL))
      \< (add-token (if (match \=) :LESS_EQUAL :LESS))
      \> (add-token (if (match \=) :GREATER_EQUAL :GREATER))
      (error @*line "Unexpected character."))))

(defn scan-tokens []
  (while (not (is-at-end))
    (reset! *start @*current)
    (scan-token))
  
  (swap! *tokens conj (Token. :EOF "" nil @*line))
  @*tokens)

;; main
(defn run [source]
  (if @*had-error
    (println "error, exit here")
    (println (format "pass %s to scanner" source))))

(defn run-file [path]
  (-> path slurp run))

(defn -main [& args]
  (println "Usage: clox [script]"))
