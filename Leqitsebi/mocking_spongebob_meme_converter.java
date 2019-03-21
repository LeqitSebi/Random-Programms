package Leqitsebi;

public class mocking_spongebob_meme_converter {

    public static void main(String[] args) {
        System.out.println(converter("Stefan ist gut in SEW!"));
    }
    public static String converter(String sentence){
        sentence = sentence.toLowerCase();
        StringBuilder output= new StringBuilder();
        int rnd;
        for (int i = 0; i < sentence.length(); i++) {
            rnd = (int)(Math.random() * 17);
            String c = "" + sentence.charAt(i);
            if(rnd%2==0){
                c=c.toUpperCase();
            }
            output.append(c);
        }
        return output.toString();
    }
}
