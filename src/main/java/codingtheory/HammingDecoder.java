package codingtheory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Easy Easy
// very procedural
// LLM wrote this

public class HammingDecoder {

    GF2Math math;
    HammingCodeGenerator generator;
    int[][] H_T; // Transpose of the H matrix
    Map<String, Integer> syndromeMap;

    // decoder for specific hamming code
    public HammingDecoder(int p) {
        this.generator = new HammingCodeGenerator(p);
        this.math = new GF2Math();
        
        this.H_T = math.transpose(generator.H);
        
        this.syndromeMap = buildSyndromeMap();
    }

    /**
     * Pre-computes the mapping from a syndrome (e.g., "[1, 0, 1]")
     * to the bit position that caused it.
     */
    private Map<String, Integer> buildSyndromeMap() {
        Map<String, Integer> map = new HashMap<>();
        
        // The syndrome for "no error" is [0, 0, ..., 0]
        map.put(Arrays.toString(new int[generator.p]), -1); // -1 means no error

        // The syndrome for a 1-bit error at position 'i' is just
        // the i-th row of H_T (which is the i-th column of H).
        for (int i = 0; i < generator.n; i++) {
            // Get the i-th column of H
            int[] syndrome = new int[generator.p];
            for (int j = 0; j < generator.p; j++) {
                syndrome[j] = generator.H[j][i];
            }
            map.put(Arrays.toString(syndrome), i);
        }
        return map;
    }

    /**
     * Detects and corrects a single-bit error in a received word.
     *
     * @param receivedWord An n-bit (possibly corrupted) codeword.
     * @return The n-bit *corrected* codeword.
     */
    public int[] decode(int[] receivedWord) {
        if (receivedWord.length != generator.n) {
            throw new IllegalArgumentException(
                "Received word length (" + receivedWord.length + ") " +
                "does not match code's n-value (" + generator.n + ")."
            );
        }

        // 1. Calculate the syndrome: S = Y * H^T
        int[] syndrome = math.vectorMatrixMultiply(receivedWord, this.H_T);
        String syndromeKey = Arrays.toString(syndrome);

        // 2. Look up the error position
        if (!syndromeMap.containsKey(syndromeKey)) {
            // This should not happen for a single-bit error
            System.err.println("Uncorrectable error! Syndrome: " + syndromeKey);
            return receivedWord; // Return as-is
        }

        int errorPosition = syndromeMap.get(syndromeKey);

        // 3. Correct the error (if any)
        if (errorPosition == -1) {
            // Syndrome was [0, 0, 0], no error found.
            return receivedWord;
        } else {
            // Error found at errorPosition. Flip the bit.
            System.out.println("...Error detected at bit " + errorPosition);
            int[] correctedWord = receivedWord.clone();
            correctedWord[errorPosition] = math.add(correctedWord[errorPosition], 1);
            return correctedWord;
        }
    }

    /**
     * Extracts the k-bit message from a corrected n-bit systematic codeword.
     *
     * @param correctedCodeword The n-bit corrected codeword.
     * @return The k-bit original message.
     */
    public int[] extractMessage(int[] correctedCodeword) {
        // For a systematic code, the message is just the first k bits.
        return Arrays.copyOfRange(correctedCodeword, 0, generator.k);
    }
}