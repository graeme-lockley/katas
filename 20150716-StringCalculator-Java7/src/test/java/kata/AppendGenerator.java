package kata;

import java.util.List;

class AppendGenerator implements Generator<String> {
    private Generator generator;

    public AppendGenerator(Generator generator) {
        this.generator = generator;
    }

    @Override
    public String next() {
        Object object = generator.next();

        if (object instanceof List) {
            StringBuilder sb = new StringBuilder();
            List list = (List) object;
            for (Object item : list) {
                sb.append(item.toString());
            }
            return sb.toString();
        } else {
            return object.toString();
        }
    }
}
