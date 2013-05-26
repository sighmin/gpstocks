
package gpfinance.tree;

import gpfinance.U;
import gpfinance.datatypes.Decision;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class Node {
    public Node left;
    public Node right;
    public short depth = 0;
    
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
        U.m(prefix + (isTail ? "└── " : "├── ") + this.toString());
        if (left != null)
            left.print(prefix + (isTail ? "    " : "│   "), left == null);
        if (right != null)
            right.print(prefix + (isTail ? "    " : "│   "), right == null);
    }
    
    @Override
    public Node clone(){
        return new Node(this.left.clone(), this.right.clone());
    }
    
    public Decision eval(double[] indicators){
        return null;
    }
}
