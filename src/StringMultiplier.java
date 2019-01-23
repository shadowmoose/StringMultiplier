import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a "special" class (aka: A Joke Class), which multiplies integers using *only* Strings.
 *      ... Like, literally, ONLY Strings.
 *      I wrote this whole class without my number keys.
 * It doesn't use any ints/doubles/floats/etc, and it doesn't request any from the JVM by any means.
 *      That means that any function/method which returns a number is not allowed either.
 * I also didn't type any numbers in this class, *anywhere*.
 *
 * As a bonus, I decided that chars are cheating, since they're thinly-veiled ints anyways.
 *      Wouldn't want to accidentally cast a char into an int, after all.
 *
 * As a warning:
 *      This is probably the worst code I've ever written.
 *      Almost every single thing done here super inefficient. It's really slow. Like, "glacier" slow.
 *
 *  Notes:
 *       Yes, I know String.toCharArray exists. Characters are cheating.
 *       There are a lot of parts that could be way more optimized, even within the self-imposed limitations.
 *            Off the top of my head, generators would be better for avoiding heap overflows.
 *            There are a lot of places where handling Strings could be optimized.
 *       I simply don't have time to fully optimize the logic.
 */
public class StringMultiplier {
    private final String[] table;
    private final String ten = "iiiiiiiiii";
    public String zero = null;

    /**
     * Create an instance of the StringMultiplier, and initialize the numbers it will need.  <br>
     *     Good luck.
     */
    public StringMultiplier(){
        // If I'm not going to type a number, I need to find them from "somewhere".
        // Just getting an actual integer passed to me from the JVM is cheating,
        // 	so here's some insane code to generate a full set from only String values instead.
        StringBuilder set = new StringBuilder();
        boolean need_more = true;
        while(need_more) {
            String prod = new Object().toString().replaceAll("[^\\d]", "");
            for(String s : prod.split("")){
                if(!set.toString().contains(s)){
                    set.append(s);
                }
            }
            String check = ten;
            for(String ignored : set.toString().split("")){
                check = check.replaceFirst("i", "");
            }
            need_more = !check.equals("");
        }
        String[] list = set.toString().split("");
        Arrays.sort(list);
        StringBuilder tmp = new StringBuilder();
        boolean skip=true;
        for(String s : list){
            if(skip){
                skip = false;
                this.zero = s;
            }else {
                tmp.append(s);
            }
        }
        this.table = tmp.toString().split("");
        if(this.zero == null)
            throw new IndexOutOfBoundsException("Invalid zero value.");
    }

    public List<String> getTable(){
        ArrayList<String> ret = new ArrayList<>();
        ret.addAll(Arrays.asList(this.table));
        return ret;
    }

    /**
     * Convert a simple single digit into a string.
     * @param num The digit to convert.
     * @return A sequence of characters.
     */
    private String simpDigitToSequence(String num){
        StringBuilder out = new StringBuilder();
        for(String n : table){
            out.append("i");
            if(n.equals(num))
                return out.toString();
        }
        return "";
    }

    /**
     * Transform a "complex number" (any number greater than nine) into a sequence of characters.
     * @param num The number to transform.
     * @return A sequence representing the full number.
     */
    private String complexNumberToSequence(String num){
        StringBuilder out = new StringBuilder();
        String mult = "i";
        List<String> rev = Arrays.asList(num.split(""));
        Collections.reverse(rev);
        for(String p : rev ){
            String conv = simpDigitToSequence(p);
            out.append(multiplySquence(conv, mult));
            mult = multiplySquence(mult, ten);
        }
        return out.toString();
    }

    /**
     * StringMultiplier two sequences together.
     * @param seq_x The first sequence of characters.
     * @param seq_y The second sequence of characters.
     * @return The sequence product of multiplication.
     */
    private String multiplySquence(String seq_x, String seq_y){
        StringBuilder out = new StringBuilder();
        if(seq_x.equals("") || seq_y.equals(""))
            return "";
        for(String ignored : seq_x.split(""))
            out.append(seq_y);
        return out.toString();
    }


    /**
     * StringMultiplier the two given strings, and return a sequence representing the product.
     * @return A sequence, representing the product.
     */
    private String multiplyNumbersAsSequence(String x, String y){
        return multiplySquence(complexNumberToSequence(x), complexNumberToSequence(y));
    }

    /**
     * Convert the given sequence String to a readable number.  <br>
     * Only works on sequences which resolve to numbers <= nine.
     * @return The single-digit number this sequence represents.
     */
    private String sequenceToDigit(String seq){
        if(seq.equals(""))
            return zero;
        for(String i : table){
            if(simpDigitToSequence(i).equals(seq))
                return i;
        }
        throw new IndexOutOfBoundsException("Cannot map the given simple sequence to a number. " + seq);
    }

    /**
     * Convert the given sequence into a full, human-readable number.
     * @return The complete number which represents this sequence.
     */
    public String sequenceToNumber(String sequence){
        StringBuilder out = new StringBuilder();
        String last = "";
        try{
            return sequenceToDigit(sequence); // Attempt simple sequence conversion to save time.
        }catch(IndexOutOfBoundsException ignored){}

        while(!sequence.equals("")){
            String high = ten;
            StringBuilder pad = new StringBuilder(zero);
            if(!sequence.contains(high)){
                high = "i";
                pad = new StringBuilder();
            }
            while(sequence.contains(multiplySquence(high, ten))){
                high = multiplySquence(high, ten);
                pad.append(zero);
            }
            if(!last.equals("")){
                String tm = ten;
                while(!multiplySquence(high, tm).equals(last)){
                    out.append(zero);
                    tm = multiplySquence(tm, ten);
                }
            }
            last = high;
            StringBuilder place = new StringBuilder();
            while(sequence.contains(high)){
                place.append("i");
                sequence = sequence.replaceFirst(high, "");
            }
            out.append(sequenceToDigit(place.toString()));
            if(sequence.equals("")){
                out.append(pad);
            }
        }
        if(out.toString().equals(""))
            out.append(zero);
        return out.toString();
    }

    /**
     * My no-number-using ripoff of str.charAt(). It uses a sequence instead of an index.
     * Obviously this is infinitely less optimal.
     * @param input The haystack to search in
     * @param counter A sequence representing the desired position.
     * @return A String representing the character at the desired position.
     */
    private String strAt(String input, String counter){
        for(String s : input.split("")){
            if(counter.equals(""))
                return s;
            counter = counter.replaceFirst("i", "");
        }
        return null;
    }

    /**
     * Adds the given List of number strings together. Assumes the strings are still in reverse order.
     * @param numbers The List of numbers (as Strings) to add together vertically, following long-format multiplication.
     * @return The total value, as a string.
     */
    private String verticalSumNumbers(List<String> numbers){
        StringBuilder out = new StringBuilder();
        StringBuilder remainder = new StringBuilder();
        List<Iterator> iterators = numbers.stream()
                .map(str->Arrays.asList(str.split(""))).map(List::iterator).collect(Collectors.toList());
        while(true) {
            List<String> chars = new ArrayList<>();
            for(Iterator i : iterators)
                if(i.hasNext())
                    chars.add((String)i.next());
            if(chars.isEmpty())
                break;
            StringBuilder num = new StringBuilder();
            for(String n : chars){
                num.append(complexNumberToSequence(n));
            }
            if(!remainder.toString().equals(""))
                num.append(remainder);
            remainder = new StringBuilder();
            while(num.toString().contains(ten)){
                remainder.append("i");
                num = new StringBuilder(num.toString().replaceFirst(ten, ""));
            }
            out.append(sequenceToNumber(num.toString()));
        }
        if(!remainder.toString().equals(""))
            out.append(sequenceToNumber(remainder.toString()));
        return out.reverse().toString();
    }

    /**
     * Checks if the given numeric String should evaluate to zero.
     * @param number The number to check against.
     * @return If the number is ultimately zero.
     */
    public boolean isZero(String number){
        return Arrays.stream(number.split("")).noneMatch(s->!s.equals(zero) && !s.equals(""));
    }

    /**
     * Use old-school Long-Form Multiplication to more "rapidly" multiply two values as Strings.  <br>
     * Works for any two round values represented as Strings.
     * @return The product, as a String.
     */
    public String multiply(String x, String y){
        // start with some basic number cleanup.
        boolean negative = (x.contains("-") && !y.contains("-")) || (!x.contains("-") && y.contains("-"));
        x = x.replace("-", "");
        y = y.replace("-", "");
        if(isZero(x) || isZero(y))
            return zero;
        String smaller, larger;
        String count = "";
        while(true){
            // This multiplication really works better if we know the smaller value.
            if(strAt(x, count) == null){
                smaller = x;
                larger = y;
                break;
            }
            if(strAt(y, count) == null){
                smaller = y;
                larger = x;
                break;
            }
            count+="i";
        }
        List<String> top = Arrays.asList(larger.split(""));
        Collections.reverse(top);
        List<String> bot = Arrays.asList(smaller.split(""));
        Collections.reverse(bot);

        // The top & bottom numbers are now arranged optimally, time to multiply:
        ArrayList<String> numbers = new ArrayList<>();
        StringBuilder total = new StringBuilder();
        StringBuilder pad = new StringBuilder();
        for(String b : bot){
            StringBuilder carryover = new StringBuilder();
            total.append(pad);
            for(String t : top){
                String product = multiplyNumbersAsSequence(b, t);
                if(!carryover.toString().equals("")){
                    product += carryover.toString();
                    carryover = new StringBuilder();
                }

                while(product.contains(ten)){
                    carryover.append("i");
                    product = product.replaceFirst(ten, "");
                }
                total.append(sequenceToNumber(product));
            }
            if(!carryover.toString().equals(""))
                total.append(sequenceToNumber(carryover.toString()));
            numbers.add(total.toString());
            total = new StringBuilder();
            pad.append(zero);
        }
        String ret = verticalSumNumbers(numbers);
        if(negative)
            ret = "-"+ret;
        return ret;
    }
}
