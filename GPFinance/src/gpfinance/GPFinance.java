
package gpfinance;


/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class GPFinance {
    public static void main(String[] args) {
        // parse args
        String cmds = "test";
        for (String s : args)
            cmds += s + " ";
        
        // dispatch simulation
        if (cmds.contains("test")){
            test();
        } else if (cmds.contains("run")){
            
        } else {
            usage();
        }
    }
    
    public static void usage(){
        System.out.println(""
                + "GPFinance usage:\n"
                + "test\n"
                + "  java -jar GPFinance.jar test\n"
                + "run\n"
                + "  java -jar GPFinance.jar run [fundamental|technical]");
    }
    
    public static void test(){
        new Simulator().test();
    }
}
