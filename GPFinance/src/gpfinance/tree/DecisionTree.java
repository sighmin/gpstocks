
package gpfinance.tree;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class DecisionTree {
    
    private CriteriaNode root;
    
    public DecisionTree(){
        
    }
    
    public String printInorder(Node node){
        String tmp = "";
        if (node != null){
            tmp += printInorder(node.left);
            tmp += node.toString() + ", ";
            tmp += printInorder(node.right);
        }
        return tmp;
    }
    
    @Override
    public String toString(){
        String tmp = "";
        if (root != null){
            return printInorder(root);
        }
        return tmp;
    }
}
