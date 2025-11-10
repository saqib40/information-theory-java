package sourcecoding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ShannonFano {
    static class ShannonSymbol implements Comparable<ShannonSymbol> {
        public ShannonSymbol(char symbol, int freq) {
            this.symbol = symbol;
            this.freq = freq;
        }
        char symbol;
        int freq;
        String code = "";
        @Override
        public int compareTo(ShannonSymbol other) {
            return Integer.compare(other.freq, this.freq); // decreasing order
        }
    }

    static ArrayList<ShannonSymbol> frequencies(String str) {
        HashMap<Character,Integer> hashMap = new HashMap<>();
        for(int i=0; i<str.length(); i++) {
            char symbol = str.charAt(i);
            if(hashMap.containsKey(symbol)) {
                hashMap.put(symbol, hashMap.get(symbol)+1);
            } else {
                hashMap.put(symbol, 1);
            }
        }
        ArrayList<ShannonSymbol> list = new ArrayList<>();
        for(HashMap.Entry<Character,Integer> x : hashMap.entrySet()) {
            ShannonSymbol sym = new ShannonSymbol(x.getKey(), x.getValue());
            list.add(sym);
        }
        Collections.sort(list);
        return list;
    }

    static int findBestSplitIndex(ArrayList<ShannonSymbol> list) {
        int totalFre = 0;
        for(ShannonSymbol s : list) {
            totalFre += s.freq;
        }
        int bestIndex = 0;
        int minDifference = Integer.MAX_VALUE;
        int leftSum = 0;
        for(int i=0; i<list.size(); i++) {
            leftSum += list.get(i).freq;
            int rightSum = totalFre-leftSum;
            int difference = Math.abs(leftSum-rightSum);
            if(difference<=minDifference) {
                minDifference = difference;
                bestIndex = i+1;
            }
        }
        return bestIndex;
    }

    static void shannonFanoEncode(String str) {
        HashMap<Character, String> map = new HashMap<>();
        ArrayList<ShannonSymbol> sortedList = frequencies(str);
        shannonFanoEncode(map, sortedList);
        StringBuilder finalEncodedString = new StringBuilder();
        for (char c : str.toCharArray()) {
            finalEncodedString.append(map.get(c));
        }
        System.out.println("Encoded string (in binary) for: " + str + " is: " + finalEncodedString.toString());
        System.out.println("The map for decoding looks like this: ");
        for(HashMap.Entry<Character,String> x : map.entrySet()) {
            System.out.println("Key: " + x.getKey() + " Value: " + x.getValue());
        }
    }

    static void shannonFanoEncode(HashMap<Character, String> map, ArrayList<ShannonSymbol> list) {
        if(list.size() == 1) {
            ShannonSymbol s = list.get(0);
            map.put(s.symbol, s.code);
            return;
        }
        if(list.isEmpty()) {
            return;
        }

        int splitIndex = findBestSplitIndex(list);

        ArrayList<ShannonSymbol> group1 = new ArrayList<>(list.subList(0, splitIndex));
        ArrayList<ShannonSymbol> group2 = new ArrayList<>(list.subList(splitIndex, list.size()));

        for (ShannonSymbol s : group1) {
            s.code += "1";
        }
        for (ShannonSymbol s : group2) {
            s.code += "0";
        }

        shannonFanoEncode(map, group1);
        shannonFanoEncode(map, group2);
    }

    public static void main(String[] args) {
        shannonFanoEncode("AAAABBBCCD");
    }
}
