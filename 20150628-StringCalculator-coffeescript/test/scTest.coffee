chai = require 'chai'
chai.should()

{SC} = require '../src/sc'

forAll = (gen) -> (predicate) -> predicate(gen()) for lp in [1..1000]
forAll2 = (gen1, gen2) -> (predicate) -> predicate(gen1(), gen2()) for lp in [1..1000]
integerInRange = (minInt, maxInt) -> Math.round((Math.random() * (maxInt - minInt)) + minInt)
integers = () -> integerInRange(-2000, 2000)
nonNegativeIntegers = () -> integerInRange(0, 2000)
nonEmptyListOf = (gen) -> () -> [1..integerInRange(1, 10)].map((x) -> gen())
oneOf = (lst) -> () -> lst[integerInRange(0, lst.length-1)]
separator = () ->
  loop
    result = String.fromCharCode(integerInRange(32, 127))
    break unless result in ['-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '[']
  result
stringSeparator = () -> nonEmptyListOf(separator)().join('')

describe 'StringCalculator', ->
  sc = new SC()

  it 'Given an empty string should return 0', ->
    sc.add('').should.equal 0

  it 'Given a non-negative integer should return its value if less than 1001 otherwise 0', ->
    forAll(nonNegativeIntegers) ((n) ->
      result = if n < 1001 then n else 0
      sc.add(n.toString()).should.equal result
    )

  it 'Given non-negative integers separated by a comma or newline should return the sum of all numbers less than 1001', ->
    forAll(nonEmptyListOf(nonNegativeIntegers)) ((ns) ->
      sc.add(mkString(ns, [',', '\n'])).should.equal sum(ns)
    )

  it 'Given non-negative integers separated by a custom single character separator should return the sum of all numbers less than 1001', ->
    forAll2(nonEmptyListOf(nonNegativeIntegers), separator) ((ns, sep) ->
      sc.add('//' + sep + '\n' + ns.join(sep)).should.equal sum(ns)
    )

  it 'Given non-negative integers separated by multiple multi-character separator should return the sum of all numbers less than 1001', ->
    forAll2(nonEmptyListOf(nonNegativeIntegers), nonEmptyListOf(stringSeparator)) ((ns, seps) ->
      sc.add('//[' + seps.join('][') + ']\n' + mkString(ns, seps)).should.equal sum(ns)
    )

  it 'Given integers separated by a comma or newline with at least one negative should throw an exception with the negative numbers in the exception message', ->
    forAll(nonEmptyListOf(integers)) ((ns) ->
      if ns.filter((x) -> x < 0).length > 0
        try
          sc.add(mkString(ns, [',', '\n']))
          fail
        catch error
          error.should.equal('Negatives not allowed: ' + ns.filter((x) -> x < 0).join(','))
    )


sum = (ns) -> ns.filter((x) -> x < 1001).reduce(((x, y) -> x + y), 0)

mkString = (xs, seps) ->
  if xs.length is 0 then ''
  else if xs.length is 1 then xs[0].toString()
  else xs[0].toString() + oneOf(seps)() + mkString(xs[1..], seps)