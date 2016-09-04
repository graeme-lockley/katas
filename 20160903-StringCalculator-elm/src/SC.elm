module SC exposing (sum)

import String
import Regex

sum : String -> Result (List Int) Int
sum input =
    let
        numbers = parse input
        isNegative = \n -> n < 0
    in
        if (List.any isNegative numbers) then
            Result.Err (List.filter isNegative numbers)
        else
            Result.Ok (List.foldr (+) 0 (List.filter (\n -> n < 1001) numbers))


parse : String -> List Int
parse input =
    if (String.startsWith "//[" input) then
        let
            newlineIndex = safeHead 0 <| String.indexes "\n" input
            numbers = String.dropLeft (newlineIndex + 1) input
            separators = Regex.split Regex.All (Regex.regex "\\]\\[") <| String.slice 3 (newlineIndex - 1) input
        in
            tokenize numbers separators
    else if (String.startsWith "//" input) then
        tokenize (String.dropLeft 4 input) [String.slice 2 3 input]
    else
        tokenize input [",", "\n"]


tokenize : String -> List String -> List Int
tokenize input separators =
    let
        escapeSeparators = List.map Regex.escape separators
        headEscapeSeparators = safeHead "," escapeSeparators
        tailEscapeSeparators = safeTail [] escapeSeparators
        separatorsRegex = Regex.regex (List.foldr (\a -> \b -> a ++ "|" ++ b) headEscapeSeparators tailEscapeSeparators)
        tokens = Regex.split Regex.All separatorsRegex input
    in
        List.map (stringToInt 0) tokens


stringToInt : Int -> String -> Int
stringToInt default input =
    Result.withDefault default (String.toInt input)


safeHead : a -> List a -> a
safeHead default input =
    Maybe.withDefault default (List.head input)


safeTail : List a -> List a -> List a
safeTail default input =
    Maybe.withDefault default (List.tail input)
