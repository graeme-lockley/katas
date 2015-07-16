package kata;

class IntegerInRange implements Generator<Integer> {
    private final int min;
    private final int max;

    public IntegerInRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Integer next() {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
