package sourcecoding;

public class RLE {
    static String rle(String str) {
        if(str.isEmpty()) {
            return "";
        }
        StringBuilder ans = new StringBuilder();
        int count = 1;
        char ch = str.charAt(0);
        for(int i=1; i<str.length(); i++) {
            if(str.charAt(i) == ch) {
                count++;
            } else {
                ans.append(ch);
                ans.append(count);
                count = 1;
                ch = str.charAt(i);
            }
        }
        ans.append(ch);
        ans.append(count);
        return ans.toString();
    }
    public static void main(String[] args) {
        System.out.println(rle("WWWWWAAAAZZZZM"));
    }
}
