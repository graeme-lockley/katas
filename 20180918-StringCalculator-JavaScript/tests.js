const add = require("./SC");


const Gen = {
    integer: (min, max) =>
        () => Math.floor(Math.random() * (max - min + 1)) + min,

    listOf: (gen) =>
        () => Array(Gen.integer(0, 10)()).fill(0).map(gen),

    nonEmptyListOf: (gen) =>
        () => Array(Gen.integer(1, 10)()).fill(0).map(gen),

    map: (gen, f) =>
        () => f(gen()),

    filter: (gen, predicate) =>
        () => {
            const value =
                gen();

            return predicate(value) ? value : Gen.filter(gen, predicate)();
        },

    forall: (gen, predicate) => {
        for (let lp = 0; lp < 1000; lp += 1) {
            const ns =
                gen();

            if (!predicate(ns)) {
                throw new Error(ns.toString());
            }
        }
        return true;
    },

    forall2: (gen1, gen2, predicate) => {
        for (let lp = 0; lp < 1000; lp += 1) {
            const ns1 =
                gen1();

            const ns2 =
                gen2();

            if (!predicate(ns1, ns2)) {
                throw new Error([ns1, ns2].toString());
            }
        }
        return true;
    }
};


const INTEGERS =
    Gen.integer(-1200, 1200);

const POSITIVE_INTEGERS =
    Gen.integer(0, 1200);

const SEPARATORS =
    Gen.filter(Gen.map(Gen.integer(32, 65535), f => String.fromCharCode(f)), sep => (sep < "0" || sep > "9") && sep !== "-" && sep !== "[");

const STRING_SEPARATORS =
    Gen.map(Gen.nonEmptyListOf(SEPARATORS), seps => seps.join(""));

const LIST_OF_INTEGERS =
    Gen.listOf(INTEGERS);

const LIST_OF_POSITIVE_INTEGERS =
    Gen.listOf(POSITIVE_INTEGERS);


const tests = [
    {
        name: "given a blank string should return 0",
        assert: () => add("") === 0
    },
    {
        name: "given a positive integer should return its value if less than 1001 otherwise 0",
        assert: () => Gen.forall(POSITIVE_INTEGERS, n =>
            add(n.toString()) === (n < 1001 ? n : 0)
        )
    },
    {
        name: "given positive integers separated with a comma or newline should return the sum of all less than 1001",
        assert: () => Gen.forall(LIST_OF_POSITIVE_INTEGERS, ns =>
            add(mkString(ns, [",", "\n"])) === sum(ns)
        )
    },
    {
        name: "given positive integers separated with a custom single character separator should return the sum of all less than 1001",
        assert: () => Gen.forall2(LIST_OF_POSITIVE_INTEGERS, SEPARATORS, (ns, sep) =>
            add(`//${sep}\n${ns.join(sep)}`) === sum(ns)
        )
    },
    {
        name: "given positive integers separated with custom multiple multi-character separator should return the sum of all less than 1001",
        assert: () => Gen.forall2(LIST_OF_POSITIVE_INTEGERS, Gen.nonEmptyListOf(STRING_SEPARATORS), (ns, seps) =>
            add(`//[${seps.join("][")}]\n${mkString(ns, seps)}`) === sum(ns)
        )
    },
    {
        name: "given integers with at least a single negative should throw an exception with all of the negatives",
        assert: () => Gen.forall(Gen.filter(LIST_OF_INTEGERS, ns => ns.find(n => n < 0)), ns =>
            catchError(() => add(ns.join(","))) === ns.filter(n => n < 0).join(", ")
        )
    }
];

const sum = ns =>
    ns.filter(n => n < 1001).reduce((x, y) => x + y, 0);


const mkString = (ns, seps) =>
    ns.length === 0 ? ""
        : ns.length === 1 ? ns[0].toString()
        : ns[0].toString() + seps[Gen.integer(0, seps.length - 1)()] + mkString(ns.slice(1), seps);


const catchError = f => {
    try {
        f();
        return null;
    } catch (e) {
        return e.message;
    }
};

console.log(
    tests
        .map(test => {
            try {
                return {name: test.name, success: test.assert()};
            } catch (e) {
                return {name: test.name, success: false, reason: e};
            }
        })
        .map(result => result.success ? `\x1b[32m${result.name}\x1b[0m` : `\x1b[31m${result.name}${result.reason ? `: ${result.reason}: ${result.reason.stack}` : ""}\x1b[0m`)
        .join("\n"));
