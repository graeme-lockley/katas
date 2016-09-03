module Test.Main where

import Prelude hiding (add)

import Control.Monad.Eff.Console (CONSOLE)
import Node.Process (PROCESS)
import Control.Monad.Eff (Eff)
import Test.Spec                  (describe, it)
import Test.Spec.Runner           (run)
import Test.Spec.Assertions       (shouldEqual)
import Test.Spec.Reporter.Console (consoleReporter)

import StringCalculator (add)
import Data.Either (Either(..))

main :: forall t. Eff (process :: PROCESS, console :: CONSOLE | t) Unit
main = run [consoleReporter] do
    describe "string calculator" do
        it "given a blank string should return 0" do
            add "" `shouldEqual` Right 0
        it "given a number should return its value" do
            add "123" `shouldEqual` Right 123
        it "given numbers separated with a comma should return the sum of all numbers less than 1001" do
            add "1,2,2001,3" `shouldEqual` Right 6
        it "given numbers separated with a comma or newline should return the sum of all numbers less than 1001" do
            add "1,2\n3,3001" `shouldEqual` Right 6
        it "given numbers separated with a single character custom separator should return the sum of all numbers less than 1001" do
            add "//;\n1;1001;2;3;4" `shouldEqual` Right 10
        it "given numbers separated with a multiple multi-character custom separator should return the sum of all numbers less than 1001" do
            add "//[===][---]\n1===2---20001===3---4" `shouldEqual` Right 10
        it "given numbers with at least one negative should return an error with the negative numbers of all numbers less than 1001" do
            add "1,-2,3,-4" `shouldEqual` Left [-2, -4]
