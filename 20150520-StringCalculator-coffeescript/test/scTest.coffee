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
integerInRange = (minInt, maxInt) -> Math.round((Math.random() * (maxInt - minInt)) + minInt)
integers = () -> integerInRange(-2000, 2000)
nonNegativeIntegers = () -> integerInRange(0, 2000)
nonEmptyListOf = (gen) -> () -> [1..integerInRange(0, 10)].map((_) -> gen())
oneOf = (xs) -> () -> xs[integerInRange(0, xs.length - 1)]
separators = () -> genFilter((() -> String.fromCharCode(integerInRange(33, 127))), (ch) -> !(ch >= '0' && ch <= '9') && ch != '-' && ch != '[' && ch != ']')
stringSeparators = () -> [1..integerInRange(1, 10)].map((_) -> separators()).reduce((x, y) -> x + y)

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
    forAll(nonEmptyListOf(nonNegativeIntegers)) ((xs) ->
      sc.add(xs.join(',')).should.equal sum(xs)
    )

  it 'given numbers separated with a comma or newline should return the sum', ->
    forAll(nonEmptyListOf(nonNegativeIntegers)) ((xs) ->
      sc.add(mkList(xs, [',', ':'])).should.equal sum(xs)
    )

  it 'given numbers separated with a single character should return the sum', ->
    forAll2(nonEmptyListOf(nonNegativeIntegers), separators) ((xs, sep) ->
      sc.add('//' + sep + '\n' + xs.join(sep)).should.equal sum(xs)
    )

  it 'given numbers separated with a single multi-character should return the sum', ->
    forAll2(nonEmptyListOf(nonNegativeIntegers), stringSeparators) ((xs, sep) ->
      console.log('//[' + sep + ']\n' + xs.join(sep))
      sc.add('//[' + sep + ']\n' + xs.join(sep)).should.equal sum(xs)
    )

  it 'given numbers separated with a multiple multi-character should return the sum', ->
    forAll2(nonEmptyListOf(nonNegativeIntegers), nonEmptyListOf(stringSeparators)) ((xs, seps) ->
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

