package codingtheory;

public class HammingEncoder {
    // super easy
    GF2Math math;
    HammingCodeGenerator generator;

    public HammingEncoder(int p) {
        this.generator = new HammingCodeGenerator(p);
        this.math = new GF2Math();
    }

    public int[] encode(int[] message) {
        // Check if the message length is correct
        if (message.length != generator.k) {
            throw new IllegalArgumentException(
                "Message length (" + message.length + ") " +
                "does not match code's k-value (" + generator.k + ")."
            );
        }
        return math.vectorMatrixMultiply(message, generator.G);
    }
}
