
package gpfinance;

import java.util.Random;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class U {

    /**
     * Utility class configuration parameters.
     */
    public static final double MAX = 100.0;
    public static final double MIN = -100.0;
    public static final boolean debug = true;
    public static double MEAN = 0.0;
    public static Random random = new Random();

    /**
     * Set of methods to print the object passed. p() - prints object with no
     * new line, if debugging pl() - prints object with a new line, if debugging
     * m() - prints the object regardless of debug mode
     */
    public static void p(Object o) {
        if (debug) {
            System.out.print(o);
        }
    }

    public static void pl(Object o) {
        if (debug) {
            System.out.println(o);
        }
    }

    public static void pl() {
        if (debug) {
            System.out.println();
        }
    }

    public static void m(Object o) {
        System.out.println(o);
    }

    /**
     * Set of methods to help with random generation of data types. chance() -
     * 50/50 chance to return true/false randomVal() - return random double in
     * range [MIN, MAX)
     */
    public static boolean chance() {
        return Math.random() < 0.5;
    }

    public static double r() {
        return Math.random();
    }

    public static int rint(int size) {
        return random.nextInt(size);
    }

    public static double randomVal() {
        return (MIN + (Math.random() * ((MAX - MIN) + 1.0)));
    }

    public static int randomTreeIndex(int size) {
        return random.nextInt(size - 1) + 1;
    }

    public static double getRandomGauss(double... DEVIATION) {
        /*if (DEVIATION.length == 0){ DEVIATION = new double[1]; DEVIATION[0] = 1.0; }
         double q, u, v, x, y;
         SecureRandom random = new SecureRandom();
         do {
         u = random.nextDouble(); v = random.nextDouble();
         if (u <= 0.0 || v <= 0.0) {
         u = 1.0;
         v = 1.0;
         }
         v = 1.7156 * (v - 0.5);
         x = u - 0.449871;
         y = Math.abs(v) + 0.386595;
         q = x * x + y * (0.19600 * y - 0.25472 * x);
         if (q < 0.27597) { break; }
         } while ((q > 0.27846) || (v * v > -4.0 * Math.log(u) * u * u));
        
         System.out.println(MEAN + DEVIATION[0] * v / u);
         return (MEAN + DEVIATION[0] * v / u);*/

        return (random.nextGaussian() * 50);
    }

}
