import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static StringMultiplier mult = null;

    public static void main(String[] args) {
        mult = new StringMultiplier();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a number:");
            String fn = br.readLine();
            System.out.print("Enter another number:");
            String sn = br.readLine();
            
            System.out.println("Multiplied: " + mult.multiply(fn, sn));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading the input!");
        }
    }
}
