package kata;

public class CharacterGenerator implements Generator<Character> {
    private final Generator<Integer> generator = new IntegerInRange(0, 255);

    @Override
    public Character next() {
        return (char) generator.next().intValue();
    }
}
