(ns sc.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn- parse-numbers-using-regex
  [input separator]
  (map #(Integer/parseInt %) (str/split input separator)))

(defn- re-quote
  [input]
  (java.util.regex.Pattern/quote input))

(defn- parse-input
  [input]
  (cond
    (= input "") '(0)
    (.startsWith input "//[") (let [index-of-newline (.indexOf input "\n")
                                    separator-string (subs input 3 (- index-of-newline 1))
                                    separator-regex (re-pattern (str/join "|" (map #(re-quote %) (str/split separator-string (re-pattern (re-quote "]["))))))
                                    string-number-input (subs input (+ index-of-newline 1) (.length input))]
                                (parse-numbers-using-regex string-number-input separator-regex))
    (.startsWith input "//") (let [separator-regex (re-pattern (re-quote (subs input 2 3)))
                                   string-number-input (subs input 4 (.length input))]
                               (parse-numbers-using-regex string-number-input separator-regex))
    :else (parse-numbers-using-regex input #",|\n")
    ))

(defn add
  "String calculator add"
  [input]
  (do (println input)
    (let [numbers (filter #(< % 1001) (parse-input input))
          negatives (filter #(< % 0) numbers)]
      (if (empty? negatives)
        (reduce + 0 numbers)
        (throw (IllegalArgumentException. (str/join "," negatives))))))
  )