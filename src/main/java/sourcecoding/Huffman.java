package sourcecoding;

import java.util.HashMap;
import java.util.PriorityQueue;

public class Huffman {
    static class HuffmanNode implements Comparable<HuffmanNode> {
        public HuffmanNode(int freq, char symbol) {
            this.freq = freq;
            this.symbol = symbol;
        }
        public HuffmanNode(HuffmanNode left, HuffmanNode right, int freq){
            this.leftChild = left;
            this.rightChild = right;
            this.freq = freq;
        }
        int freq;
        char symbol;
        HuffmanNode leftChild;
        HuffmanNode rightChild;
        @Override
        public int compareTo(HuffmanNode other) {
            return Integer.compare(this.freq, other.freq);
        }
    }

    static PriorityQueue<HuffmanNode> frequencies(String str) {
        PriorityQueue<HuffmanNode> minPQ = new PriorityQueue<>();
        // we can find frequencies in O(n) with HashMap, right???
        HashMap<Character,Integer> hashMap = new HashMap<>();
        for(int i=0; i<str.length(); i++) {
            char symbol = str.charAt(i);
            if(hashMap.containsKey(symbol)) {
                hashMap.put(symbol, hashMap.get(symbol)+1);
            } else {
                hashMap.put(symbol, 1);
            }
        }
        // now put them in minPQ
        for(HashMap.Entry<Character,Integer> x : hashMap.entrySet()) {
            HuffmanNode node = new HuffmanNode(x.getValue(), x.getKey());
            minPQ.add(node);
        }
        return minPQ;
    }

    // now let's build the tree
    // function which returns us the root of the tree
    static HuffmanNode createTree(PriorityQueue<HuffmanNode> prQue) {
        while(prQue.size()>1) {
            HuffmanNode left = prQue.poll();
            HuffmanNode right = prQue.poll();
            HuffmanNode parent = new HuffmanNode(left, right, left.freq+right.freq);
            prQue.add(parent);
        }
        return prQue.poll();
    }

    static void huffmanEncode(String toEncode) {
        // find frequencies
        PriorityQueue<HuffmanNode> freqs = frequencies(toEncode);
        // get a tree
        HuffmanNode root = createTree(freqs);
        // now time for encoding
        HashMap<Character,String> map = new HashMap<>();
        // now do the encoding
        huffmanEncode("", map, root);
        // output
        StringBuilder finalEncodedString = new StringBuilder();
        for (char c : toEncode.toCharArray()) {
            finalEncodedString.append(map.get(c));
        }
        System.out.println("Encoded string (in binary) for: " + toEncode + " is: " + finalEncodedString.toString());
        System.out.println("The map for decoding looks like this: ");
        for(HashMap.Entry<Character,String> x : map.entrySet()) {
            System.out.println("Key: " + x.getKey() + " Value: " + x.getValue());
        }
    }

    static void huffmanEncode(String encoded, HashMap<Character,String> map, HuffmanNode node) {
        if(node.leftChild == null && node.rightChild == null) {
            map.put(node.symbol, encoded);
            return;
        }
        huffmanEncode(encoded+"0", map, node.leftChild);
        huffmanEncode(encoded+"1", map, node.rightChild);
    }

    public static void main(String[] args) {
        huffmanEncode("AAAABBBCCD");
    }
}
