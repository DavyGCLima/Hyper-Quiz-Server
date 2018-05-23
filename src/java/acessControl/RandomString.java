package acessControl;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * This class provides a random string generation service that can be used as access validation tokens.
 * @author Davy Lima
 */
public class RandomString {

    /**
     * Generate a random string.
     * @return a random string.
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    
    public static final String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String alphaLower = alpha.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789";

    /**
     * Alphabet that is used in the token generet process.  
     */
    public static final String alphaNum = alpha + alphaLower + digits;

    private final Random random;

    private final char[] symbols;

    private final char[] buf;

    public RandomString(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     * @param length of string that will be generete
     * @param random to generete
     */
    public RandomString(int length, Random random) {
        this(length, random, alphaNum);
    }

    /**
     * Create an alphanumeric strings from a secure generator.<p/>
     * This method use <link RandomString(int length, Random random)/>
     * @param length of string that will be generete
     */
    public RandomString(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers. Default constructor
     */
    public RandomString() {
        this(21);
    }

}