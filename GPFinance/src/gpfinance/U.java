
package gpfinance;

import java.util.Random;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class U {
    /**
     * Utility class configuration parameters.
     */
    public static final double MAX = 100.0;
    public static final double MIN = -100.0;
    public static final boolean debug = true;
    
    /**
     * Set of methods to print the object passed.
     *   p()  - prints object with no new line, if debugging
     *   pl() - prints object with a new line, if debugging
     *   m()  - prints the object regardless of debug mode
     */
    public static void p(Object o){ if (debug) System.out.print(o); }
    public static void pl(Object o){ if (debug) System.out.println(o); }
    public static void m(Object o){ System.out.println(o); }
    
    /**
     * Set of methods to help with random generation of data types.
     *   chance()    - 50/50 chance to return true/false
     *   randomVal() - return random double in range [MIN, MAX)
     */
    public static boolean chance(){ return Math.random() < 0.5; }
    public static double randomVal() { return (MIN + (Math.random() * ((MAX - MIN) + 1.0)) * 0.85); }
    public static int randomTreeIndex(int size) { return (new Random()).nextInt(size-1) +1; }
}
