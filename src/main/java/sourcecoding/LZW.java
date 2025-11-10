package sourcecoding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZW {
    public List<Integer> compress(String uncompressed) {
        // this dictionary is what's shared between 
        // compressor and decompressor
        int dictSize = 256;
        Map<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < dictSize; i++) {
            dictionary.put("" + (char) i, i);
        }
        String P = "";
        List<Integer> output = new ArrayList<>();
        for (char C_char : uncompressed.toCharArray()) {
            String C = "" + C_char;
            String PC = P + C;
            if(dictionary.containsKey(PC)) {
                P = PC; 
            } else {
                output.add(dictionary.get(P));
                dictionary.put(PC, dictSize++);
                P = C;
            }
        }
        if(!P.isEmpty()) {
            output.add(dictionary.get(P));
        }
        return output;
    }

    public String decompress(List<Integer> compressed) {
        // LLM helped me write this one
        // not the most intuitive thing
        // but not taht hard either
        // need to spend a lil more time on this
        int dictSize = 256;
        Map<Integer, String> dictionary = new HashMap<>();
        for (int i = 0; i < dictSize; i++) {
            dictionary.put(i, "" + (char) i);
        }
        int oldCode = compressed.get(0);
        String S = dictionary.get(oldCode);
        char C = S.charAt(0);
        StringBuilder output = new StringBuilder(S);
        for (int i = 1; i < compressed.size(); i++) {
            int newCode = compressed.get(i);
            String P;
            String S_current;
            if (dictionary.containsKey(newCode)) {
                S_current = dictionary.get(newCode);
            } else {
                P = dictionary.get(oldCode);
                S_current = P + C;
            }
            output.append(S_current);
            C = S_current.charAt(0);
            P = dictionary.get(oldCode);
            dictionary.put(dictSize++, P + C);
            oldCode = newCode;
        }
        return output.toString();
    }

}
