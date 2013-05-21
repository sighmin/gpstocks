package gpfinance.tree;

import gpfinance.U;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class DecisionTree {

    private Node root = null;
    private int numNodes = 5;
    private char type = 'F';
    private enum NODE {
        PREV(0), CURR(1);
        int i;
        NODE(int i) { this.i = i; }
    };

    public DecisionTree() {
        init();
    }

    public DecisionTree(char type, int numNodes) {
        this.numNodes = numNodes;
        this.type = type;
        init();
    }

    private void init() {
        root = getRandomNode();
        root.i = 0;

        for (int i = 0; i < numNodes; ++i) {
            insertRandom();
        }
    }

    private CriteriaNode getRandomNode() {
        return type == 'F' ? CriteriaNode.getRandomFundNode() : CriteriaNode.getRandomTechNode();
    }

    private void insertRandom() {
        // Create new node
        CriteriaNode newNode = getRandomNode();

        // Get random leaf node
        Node[] nodes = getRandomTerminalNode();
        Node prev = nodes[NODE.PREV.i];
        Node node = nodes[NODE.CURR.i];

        // Replace either left or right with a random CriteriaNode
        if (prev == null) {
            // If tree has only a root node
            if (U.chance()) {
                root.left = newNode;
            } else {
                root.right = newNode;
            }
        } else {
            // Replace the correct reference of prev with the new node
            if (prev.left == node) {
                prev.left = newNode;
            } else {
                prev.right = newNode;
            }
        }
    }

    public Node[] getRandomTerminalNode() {
        Node node = root;
        Node prev = null;
        Node[] nodes = {prev, root};

        // If tree has only a root node
        if (node.isLeaf()) {
            return nodes;
        } else {
            do {
                prev = node;
                node = U.chance() ? node.left : node.right;
            } while (!node.isLeaf());

            nodes[NODE.PREV.i] = prev;
            nodes[NODE.CURR.i] = node;
        }

        return nodes;
    }
    
    public Node[] getRandomNonTerminalNode(){
        Node[] nodes = {null, root};
        
        // make list of non-terminal nodes
        ArrayList<Node[]> list = new ArrayList();
        addAllToList(list, root, null);
        
        // choose random pair
        nodes = list.get(new Random().nextInt(list.size()));
        
        return nodes;
    }
    
    public void addAllToList(ArrayList<Node[]> list, Node next, Node prev){
        if (!next.isLeaf()){
            if (next != root){
                Node[] nodes = new Node[2];
                nodes[0] = prev;
                nodes[1] = next;
                list.add(nodes);
            }
            addAllToList(list, next.left, next);
            addAllToList(list, next.right, next);
        }
    }
    
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        int size = 0;
        if (node != null) {
            size += size(node.left);
            ++size;
            size += size(node.right);
        }
        return size;
    }

    public int avgDepth() {
        return (int) (Math.log(size()) / Math.log(2));
    }

    public void print() {
        root.printChain();
    }
}
