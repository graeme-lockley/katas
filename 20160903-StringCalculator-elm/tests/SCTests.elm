module SCTests exposing (..)

import Test exposing (..)
import Expect
import String
import SC exposing (sum)


all : Test
all =
    describe "String Calculator"
        [ test "empty string results 0" <|
            \() ->
                Expect.equal (sum "") (Result.Ok 0)
        , test "an integer should return its value" <|
            \() ->
                Expect.equal (sum "1") (Result.Ok 1)
        , test "a comma separated list of non-negative integers should return the sum of all numbers less than 1001" <|
            \() ->
                Expect.equal (sum "1,2,3,99,1001") (Result.Ok 105)
        , test "a comma and newline separated list of non-negative integers should return the sum of all numbers less than 1001" <|
            \() ->
                Expect.equal (sum "1,2001,2\n3,99") (Result.Ok 105)
        , test "a custom single character separated list of non-negative integers should return the sum of all numbers less than 1001" <|
            \() ->
                Expect.equal (sum "//;\n1;2;3001;3;99") (Result.Ok 105)
        , test "multiple custom multi-character separated list of non-negative integers should return the sum of all numbers less than 1001" <|
            \() ->
                Expect.equal (sum "//[;;;][***]\n1;;;2***3001;;;3***99") (Result.Ok 105)
        , test "a comma separated list of integers with at least one negative should return an error with the negatives in the error" <|
            \() ->
                Expect.equal (sum "//;\n1;-2;3;-99") (Result.Err [-2, -99])
        ]
