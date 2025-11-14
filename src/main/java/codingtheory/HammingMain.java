package codingtheory;

// easy-peazy
// written by LLM

import java.util.Arrays;

public class HammingMain {
    public static void main(String[] args) {
        // --- 1. SETUP ---
        // Use p=3, which is the (7,4) Hamming Code
        int p = 3; 
        
        // Create an encoder and decoder. We pass the encoder's
        // generator to the decoder so they share the *exact* same
        // code matrices (G and H) without regenerating.
        HammingEncoder encoder = new HammingEncoder(p);
        HammingDecoder decoder = new HammingDecoder(p);

        System.out.println("--- Hamming (7,4) Code Test ---");
        
        // --- 2. DEFINE MESSAGE ---
        int[] message = {1, 0, 1, 1}; // Our k=4 bit message
        System.out.println("Original Message:  " + Arrays.toString(message));

        // --- 3. ENCODE ---
        int[] codeword = encoder.encode(message);
        System.out.println("Encoded Codeword:  " + Arrays.toString(codeword));

        // --- 4. SIMULATE ERROR ---
        int[] corruptedWord = codeword.clone();
        int errorBit = 3; // Let's flip the 4th bit (index 3)
        corruptedWord[errorBit] = 1 - corruptedWord[errorBit]; // Flip 0->1 or 1->0
        
        System.out.println("Corrupted Word:    " + Arrays.toString(corruptedWord));
        System.out.println("(Simulated error at bit " + errorBit + ")");

        // --- 5. DECODE ---
        int[] correctedWord = decoder.decode(corruptedWord);
        System.out.println("Corrected Codeword:" + Arrays.toString(correctedWord));

        // --- 6. EXTRACT MESSAGE ---
        int[] extractedMessage = decoder.extractMessage(correctedWord);
        System.out.println("Extracted Message: " + Arrays.toString(extractedMessage));

        // --- 7. VERIFY ---
        if (Arrays.equals(message, extractedMessage)) {
            System.out.println("\nSUCCESS: The original message was recovered!");
        } else {
            System.out.println("\nFAILURE: The message was not recovered.");
        }
    }
}