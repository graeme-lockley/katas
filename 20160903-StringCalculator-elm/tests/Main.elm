port module Main exposing (..)

import SCTests
import Test.Runner.Node exposing (run)
import Json.Encode exposing (Value)


main : Program Value
main =
    run emit SCTests.all


port emit : ( String, Value ) -> Cmd msg
