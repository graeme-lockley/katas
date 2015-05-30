class SC

	constructor: ->

	add:(input) ->
		numbers = this.parse input
		negatives = numbers.filter (x) -> x < 0

		if negatives.length is 0
			numbers.filter((x) -> x < 1001).reduce(((x, y) -> x + y), 0)
		else
			throw 'Negatives Not Allowed: ' + negatives.join ', '

	parse: (input) ->
		(switch
			when input is '' then ['0']
			when input[0..2] is '//['
				indexOfNewline = input.indexOf '\n'
				separators = new RegExp(input[3.. indexOfNewline - 2].split("][").sort().reverse().join('|'))
				input[indexOfNewline + 1 ..].split separators
			when input[0..1] is '//' then input[4..].split input[2]
			else input.split(/,|:/)).map (x) -> parseInt(x)

root = exports ? window
root.SC = SC
