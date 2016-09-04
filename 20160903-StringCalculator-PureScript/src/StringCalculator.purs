module StringCalculator (add) where

import Prelude (($), (==), (<), (-), (+), map)

import Data.Array (findIndex, filter)
import Data.Either (fromRight, Either(..))
import Data.Foldable (sum)
import Data.Int (fromString)
import Data.Maybe (fromMaybe, isJust)
import Data.String (drop, indexOf, joinWith, length, split, take)
import Data.String.Regex (noFlags, regex, split, Regex) as RE

import Partial.Unsafe (unsafePartial)


add :: String -> Either (Array Int) Int
add input =
  let
    numbers = stringToNumbers input
    isNegative = \n -> n < 0
  in
    if (exists isNegative numbers) then
      Left $ filter isNegative numbers
    else
      Right $ sum $ filter (\n -> n < 1001) numbers


stringToNumbers :: String -> Array Int
stringToNumbers s =
  map stringToInt tokens
    where input = parseInput s
          tokens = RE.split (formatRegex input.separators) input.content


type Input = {
  separators :: Array String
, content :: String
}


parseInput :: String -> Input
parseInput input =
  if (startsWith "//[" input) then
    let
      index = fromMaybe 1 $ indexOf "\n" input
    in
      { separators: split "][" $ drop 3 $ take (index - 1) input
      , content: drop (index + 1) input }
  else if (startsWith "//" input) then
    { separators: [ take 1 $ drop 2 input]
    , content: drop 4 input }
  else
    { separators: [",", "\\n"]
    , content: input }


formatRegex :: Array String -> RE.Regex
formatRegex rs =
  unsafePartial $ fromRight $ RE.regex regexString RE.noFlags
    where regexString = joinWith "|" rs


stringToInt :: String -> Int
stringToInt s =
  fromMaybe 0 $ fromString s


startsWith :: String -> String -> Boolean
startsWith sub s =
  take (length sub) s == sub


exists :: forall a. (a -> Boolean) -> Array a -> Boolean
exists f a =
  isJust $ findIndex f a
