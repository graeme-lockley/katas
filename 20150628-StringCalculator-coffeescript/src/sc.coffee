class SC

  constructor: ->

  add: (input) ->
    nums = this.parse input
    negs = nums.filter (x) -> x < 0
    if negs.length > 0
      throw 'Negatives not allowed: ' + negs.join ','
    else
      nums.filter((n) -> n < 1001).reduce ((x, y) -> x + y), 0

  parse: (input) ->
    (if input is ''
      ['0']
    else if input[0..2] is '//['
      _this = @
      indexOfNewline = input.indexOf '\n'
      input[indexOfNewline + 1 ..].split new RegExp(input[3 .. indexOfNewline - 2].split('][').sort((a, b) -> -a.localeCompare(b)).map((x) -> _this.quote(x)).join '|')
    else if input[0..1] is '//'
      input[4..].split input[2]
    else
      input.split /,|\n/).map (x) -> parseInt x

  quote: (regex) ->
    regex.replace /[.?*+^$[\]\\(){}|-]/g, "\\$&"

root = exports ? window
root.SC = SC
