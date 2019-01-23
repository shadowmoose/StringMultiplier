import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static StringMultiplier mult = null;
    private static StringBuilder counter = new StringBuilder("i");

    public static void main(String[] args) {
        mult = new StringMultiplier();
        List<String> argz = Arrays.asList(args);
        if(argz.isEmpty()){
            System.err.println("Please provide the move filepath as the first parameter.");
            return;
        }

        Pattern pattern = Pattern.compile("(\\d+)");

        try {
            BufferedReader br = new BufferedReader(new FileReader(argz.iterator().next()));
            String line;

            while((line = br.readLine()) != null) {
                line = line.replaceAll("\\s", "");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    checkNumber(matcher.group().trim());
                }
                counter.append("i");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading the given file!");
        }
    }


    private static void checkNumber(String number){
        String person = mult.sequenceToNumber(counter.toString());
        if(mult.isZero(number)){
            System.out.println("Person "+person+": You should take some melatonin.");
            return;
        }
        number = number.replaceAll("^"+mult.zero+"+", "");
        List<String> missing = mult.getTable();
        StringBuilder counter = new StringBuilder("i");
        String product = "";
        while(!missing.isEmpty()){
            product = counter.toString().equals("i")?
                    number : mult.multiply(mult.sequenceToNumber(counter.toString()), number);
            for(String c : product.split("")) {
                missing.remove(c);
            }
            counter.append("i");
        }
        System.out.println("Person "+person+": "+product);
    }

}
