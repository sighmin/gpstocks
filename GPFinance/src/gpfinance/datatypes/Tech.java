
package gpfinance.datatypes;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public enum Tech {
    EXAMPLE(0, "Example label");
    
    private int code;
    private String label;

    private Tech(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
