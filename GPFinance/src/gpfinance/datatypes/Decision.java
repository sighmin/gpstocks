
package gpfinance.datatypes;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public enum Decision {
    BUY(0, "Buy"),
    SELL(1, "Short");
    
    private int code;
    private String label;
    
    private Decision(int code, String label){
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }
    
    public String getLabel() {
        return label;
    }
    
    public static Decision getRandom(){
        return Decision.values()[Math.random() > 0.5 ? 0 : 1];
    }
}
