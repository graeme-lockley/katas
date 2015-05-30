chai = require 'chai'
chai.should()

{SC} = require '../src/sc'

genFilter = (gen, filter) -> (
	result = gen()
	while (!filter(result)) 
		result = gen()
	result
)

forAll = (gen) -> (testExpression) -> testExpression(gen()) for lp in [1..1000]
forAll2 = (gen1, gen2) -> (testExpression) -> testExpression(gen1(), gen2()) for lp in [1..1000]
integersMinMax = (minInt, maxInt) -> () -> Math.round((Math.random() * (maxInt - minInt)) + minInt)
integers = integersMinMax(-2000, 2000)
nonNegativeIntegers = integersMinMax(0, 2000)
listOfNonNegativeIntegers = () -> [1..integersMinMax(0, 10)()].map((x) -> nonNegativeIntegers())
oneOf = (xs) -> () -> xs[integersMinMax(0, xs.length-1)()]
separators = () -> genFilter((() -> String.fromCharCode(integersMinMax(33, 100)())), (ch) -> ch >= 'a' && ch <= 'z')
stringSeparators = () -> [1..integersMinMax(1, 10)()].map((x) -> separators()).reduce((x, y) -> x + y)
listOfStringSeparators = () -> [1..integersMinMax(1, 10)()].map((x) -> stringSeparators())

describe 'StringCalculator', ->
	sc = new SC()

	it 'given an empty string should return 0', ->
		sc.add('').should.equal 0

	it 'given a non-negative integer number should return the numbers value if the number is less than 1001 otherwise 0', ->
		forAll(nonNegativeIntegers) ((x) ->
			expected = if x > 1000 then 0 else x
			sc.add(x.toString()).should.equal expected
		)

	it 'given non-negative integers separated with a comma should return the sum ignoring numbers larger than 1000', ->
		forAll(listOfNonNegativeIntegers) ((xs) ->
			sc.add(xs.join(',')).should.equal sum(xs)
		)

	it 'given numbers separated with a comma or newline should return the sum', ->
		forAll(listOfNonNegativeIntegers) ((xs) ->
			sc.add(mkList(xs, [',', ':'])).should.equal sum(xs)
		)

	it 'given numbers separated with a single character should return the sum', ->
		forAll2(listOfNonNegativeIntegers, separators) ((xs, sep) ->
			sc.add('//' + sep + '\n' + xs.join(sep)).should.equal sum(xs)
		)

	it 'given numbers separated with a single multi-character should return the sum', ->
		forAll2(listOfNonNegativeIntegers, stringSeparators) ((xs, sep) ->
			sc.add('//[' + sep + ']\n' + xs.join(sep)).should.equal sum(xs)
		)

	it 'given numbers separated with a multiple multi-character should return the sum', ->
		forAll2(listOfNonNegativeIntegers, listOfStringSeparators) ((xs, seps) ->
			sc.add('//[' + seps.join('][') + ']\n' + mkList(xs, seps)).should.equal sum(xs)
		)

	it 'given numbers separated with a comma and at least one negative number should throw an exception with the negatives in the exception', ->
		(-> sc.add('1,-2,3,-4')).should.throw 'Negatives Not Allowed: -2, -4'


sum = (xs) -> xs.filter((x) -> x <= 1000).reduce(((x, y) -> x + y), 0)
mkList = (xs, seps) ->
	if xs.length is 0
		''
	else if xs.length is 1
		xs[0].toString()
	else
		xs[0].toString() + oneOf(seps)() + mkList(xs[1..], seps)

