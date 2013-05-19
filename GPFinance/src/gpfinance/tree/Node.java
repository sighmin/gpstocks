
package gpfinance.tree;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class Node {
    public Node left;
    public Node right;
    
    public Node() { }

    public Node(Node left, Node right) {
        this.left = left;
        this.right = right;
    }
}
