const add = input => {
    console.log(`>> ${input}`);

    const split = (text, sep) =>
        text ? text.split(sep) : [];

    const multiCharacterSplit = input => {
        const indexOfNewline =
            input.indexOf("\n");

        const escapeRE = text =>
            text.replace(/[*+|(){}\\.^$?]/g, "\\$&");

        const separators =
            new RegExp(input
                .substr(3, indexOfNewline - 4)
                .split("][")
                .sort((x, y) => y.length - x.length)
                .map(escapeRE)
                .join("|"));

        return input.slice(indexOfNewline + 1).split(separators);
    };

    const tokens =
        input.startsWith("//[") ? multiCharacterSplit(input)
            : input.startsWith("//") ? split(input.slice(4), input[2])
            : split(input, /[,\n]/);

    const numbers =
        tokens.map(t => parseInt(t));

    if (numbers.find(n => n < 0)) {
        throw new Error(numbers.filter(n => n < 0).join(", "))
    } else {
        return numbers.filter(n => n < 1001).reduce((x, y) => x + y, 0);
    }
};


module.exports = add;