chai = require 'chai'
chai.should()

{SC} = require '../src/sc'

describe 'String Calculator', ->
	sc = new SC()

	it 'when passed a blank string should return 0', ->
		sc.add('').should.equal 0

	it 'when passed a number should return its value', ->
		sc.add('123').should.equal 123

	it 'when passed comma separated numbers should return the sum', ->
		sc.add('1,2,3').should.equal 6

	it 'when passed comma and newline separated numbers should return the sum', ->
		sc.add('1,2\n3').should.equal 6

	it 'when passed numbers separated with a single character separator should return the sum', ->
		sc.add('//;\n1;2;3').should.equal 6

	it 'when passed numbers separated with a single regexp character separator should return the sum', ->
		sc.add('//*\n1*2*3').should.equal 6

	it 'when passed numbers separated with a single multi-character separator should return the sum', ->
		sc.add('//[---]\n1---2---3').should.equal 6

	it 'when passed numbers separated with a multiple multi-character separator should return the sum', ->
		sc.add('//[---][===]\n1---2===3').should.equal 6

	it 'when passed numbers with at least one negative number should throw an exception', ->
		(-> sc.add('1,-2,3,-4')).should.throw 'Negatives Not Allowed: -2,-4'

	it 'when passed numbers should return the sum of all numbers less than 1001', ->
		sc.add('1,1001,2,2001,3,3001').should.equal 6

