
package gpfinance;

import gpfinance.datatypes.Indicator;
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
    
    public static void mnl(Object o) {
        System.out.print(o);
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

    public static double getRangedRandomGauss(Indicator indicator) {

        double returnValue = 0.0;
        double[] means = {55.32, 55.32, -0.23, 20.31, 20.71, 3.26, 2.67, 3.29,
            39.45, 13.2, 15.68, 35.8, 22.93, 22.56, -1.58, 12.74, 12.72, 4, 16.54,
            16.35, 20.96, 25.03, 24.82, 103.68, 13.65, 13.74, 30.3, 14.6, 14.75,
            24.5, 50.2, 49.87, 1.36, 20.73, 20.46, -3.02, 2.32, 2.42, 4, 1.71, 1.83,
            4.19};
        double[] stddevs = {21.34, 21.74, 9.66, 13.02, 12.63, 51.91, 4.23, 5.34,
            125.08, 19.16, 24.44, 144.35, 13.87, 14, 10.36, 7.1, 6.85, 34.51,
            25.73, 21.3, 127.67, 30.41, 28.39, 669.44, 12.04, 10.34, 166.3, 22.52,
            21.66, 57.82, 19.09, 19.22, 17.43, 13.3, 14.24, 20.24, 1.38, 1.66,
            38.02, 1.15, 1.45, 37.03};

        for (int i = 0; i < means.length; i++) {
            if (indicator.getCode() == i) {
                returnValue = (random.nextGaussian() * stddevs[i]) + means[i];
            }
        }

       // System.out.println(indicator.getLabel() + "," + returnValue);
        return returnValue;
    }
}
