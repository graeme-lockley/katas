class SC
	constructor: ->

	add:(input) -> 
		console.log input
		numbers = (switch
			when input is '' then []
			when input[0..2] is '//[' 
				indexOfNewline = input.indexOf '\n'
				separators = new RegExp(input[3..indexOfNewline - 2].split("][").join('|'))
				input[indexOfNewline + 1..].split(separators)
			when input[0..1] is '//' then input[4..].split(input[2])
			else input.split(/,|\n/)
		).map((x) -> parseInt(x))
		
		negatives = numbers.filter((x) -> x < 0)
		if negatives.length is 0
			numbers.filter((x) -> x < 1001).reduce(((x, y) -> x + y), 0)
		else
			throw 'Negatives Not Allowed: ' + negatives.join(', ')


root = exports ? window
root.SC = SC
