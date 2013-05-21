
package gpfinance.tree;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class Node {
    public Node left;
    public Node right;
    public int i;
    
    public Node() { }

    public Node(Node left, Node right) {
        this.left = left;
        this.right = right;
    }
    
    public boolean isLeaf(){ return false; }
    
    public void printChain(){
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + this.toString());
        if (left != null)
            left.print(prefix + (isTail ? "    " : "│   "), left == null);
        if (right != null)
            right.print(prefix + (isTail ? "    " : "│   "), right == null);
    }
}
