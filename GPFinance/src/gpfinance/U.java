
package gpfinance;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class U {
    public static final double MAX = 100.0;
    public static final double MIN = -100.0;
    
    public static void p(Object o){ System.out.print(o); }
    public static void pl(Object o){ System.out.println(o); }
    
    public static boolean chance(){ return Math.random() < 0.5; }
    public static double randomVal() { return (MIN + (Math.random() * ((MAX - MIN) + 1.0)) * 0.85); }
}
