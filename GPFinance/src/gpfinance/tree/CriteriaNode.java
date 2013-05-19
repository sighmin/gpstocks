
package gpfinance.tree;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class CriteriaNode extends Node {
    public int indicator;
    public char inequality;
    public double value;
    
    public CriteriaNode(Node left, Node right) {
        super(left, right);
    }

    public CriteriaNode(Node left, Node right, int indicator, char inequality, double value) {
        super(left, right);
        this.indicator = indicator;
        this.inequality = inequality;
        this.value = value;
    }
}
