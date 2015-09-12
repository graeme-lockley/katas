(ns sc.core-test
  (:require [clojure.test :refer :all]
            [sc.core :refer :all]
            [clojure.string :as str]))

(def in-range (fn [min max] (int (+ (* (rand) (- max min)) min))))
(def integers #(in-range -2000 2000))
(def non-negative-integers #(in-range 0 2000))
(def non-empty-list-of (fn [gen] #(take (in-range 1 10) (repeatedly gen))))

(defn for-all
  [gen1 f]
  (loop [i 0]
    (if (< i 1000)
      (do
        (f (gen1))
        (recur (inc i))))))

(defn join-string
  [[item & items] seps]
  (cond
    (nil? item) ""
    (empty? items) (.toString item)
    :else (str (.toString item) (rand-nth seps) (join-string items seps))))

(deftest gen-test
  (testing "String Calculator"
    (testing "Given a non-negative should return its value if the value is less than 1001 otherwise 0"
      (for-all non-negative-integers
        (fn [n]
          (is (= (if (< n 1001) n 0) (add (.toString n)))))))
    (testing "Given a non-negative values separated by a comma or newline should return the sum of all values less than 1001"
      (for-all (non-empty-list-of non-negative-integers)
        (fn [ns]
          (is (= (reduce + (filter #(< % 1001) ns)) (add (join-string ns '("," "\n"))))))))
  ))

;(deftest string-calculator-test
;  (testing "String calculator test"
;    (testing "given an empty string should return 0"
;      (is (= 0 (add ""))))
;    (testing "given a number should return its value"
;      (is (= 5 (add "5"))))
;    (testing "given comma separated numbers should return the sum"
;      (is (= 6 (add "1,2,3"))))
;    (testing "given comma separated numbers should return the sum ignoring numbers greater than 1000"
;      (is (= 6 (add "1,1001,2,2001,3,3001"))))
;    (testing "given command and newline separated numbers should return the sum"
;      (is (= 6 (add "1\n2,3"))))
;    (testing "given numbers separated by a custom single character separator should return the sum"
;      (is (= 6 (add "//;\n1;2;3"))))
;    (testing "given numbers separated by a custom single character regex separator should return the sum"
;      (is (= 6 (add "//*\n1*2*3"))))
;    (testing "given numbers separated by a single custom multi character separator should return the sum"
;      (is (= 6 (add "//[qqq]\n1qqq2qqq3"))))
;    (testing "given numbers separated by multiple custom multi character separator should return the sum"
;      (is (= 6 (add "//[qqq][www]\n1www2qqq3"))))
;    (testing "given numbers with at least one negative number should throw an exception"
;      (is (thrown-with-msg? IllegalArgumentException #"-2,-4" (add "1,-2,3,-4"))))))
