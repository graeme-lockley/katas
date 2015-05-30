chai = require 'chai'
chai.should()

{SC} = require '../src/sc'

integersWithinRange = (min, max) -> () -> Math.round((Math.random() * (max - min) + min))
nonNegativeIntegers = () -> integersWithinRange(0, 2000)()
listOfNonNegativeIntegers = () -> [1..integersWithinRange(1, 10)()].map((x) -> nonNegativeIntegers())
oneOf = (elements) -> () -> elements[integersWithinRange(0, elements.length-1)()]
forAll = (gen) -> (expr) -> expr(gen()) for lp in [1..1000]

describe 'String Calculator', ->
	sc = new SC()

	it 'given an empty string should return 0', ->
		sc.add('').should.equal 0

	it 'given a number should return its value if it is less than 1001 otherwise 0', ->
		forAll(nonNegativeIntegers)( (x) ->
			result = if x > 1000 then 0 else x
			sc.add(x.toString()).should.equal result
		)

	it 'given numbers separated with a comma should return the sum', ->
		forAll(listOfNonNegativeIntegers) ((xs) ->
			sc.add(xs.join(',')).should.equal sum(xs)
		)

	it 'given numbers separated with a comma or newline should return the sum', ->
		forAll(listOfNonNegativeIntegers)( (xs) ->
			sc.add(mkString(xs, [',', '\n'])).should.equal sum(xs)
		)

	it 'given numbers separated with a custom character should return the sum', ->
		sc.add('//;\n1;2;3').should.equal 6

	it 'given numbers separated with a custom multi-character should return the sum', ->
		sc.add('//[---]\n1---2---3').should.equal 6

	it 'given numbers separated with a custom multi-character should return the sum', ->
		sc.add('//[---][===]\n1---2===3').should.equal 6

	it 'given numbers with at least one negative should throw an exception with the negatives', ->
		(-> sc.add('1,-2,3,-4')).should.throw 'Negatives Not Allowed: -2, -4'

	it 'given numbers should add all ignoring those greater than 1000', ->
		sc.add('//;\n1;1001;2;2001;3').should.equal 6

sum = (xs) -> xs.filter((x) -> x < 1001).reduce(((x, y) -> x + y), 0)

mkString = (xs, seps) ->
	if xs.length is 0 then ''
	else if xs.length is 1 then xs[0].toString()
	else xs[0].toString() + oneOf(seps)() + mkString(xs[1..], seps)
