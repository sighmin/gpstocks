package gpfinance.tree;

import gpfinance.U;
import java.util.Stack;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class DecisionTree {

    private Node root = null;
    private int numNodes = 5;
    private char type = 'F';

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
        Node prev = nodes[0];
        Node node = nodes[1];

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

    public int size() {
        return size(root);
    }

    public Node[] getRandomTerminalNode() {
        Node node = root;
        Node prev = null;
        Node[] nodes = {prev, root};

        // If tree has only a root node
        if (node.isLeaf()) {
            return nodes;
        }

        do {
            prev = node;
            node = U.chance() ? node.left : node.right;
        } while (!node.isLeaf());

        nodes[0] = prev;
        nodes[1] = node;
        return nodes;
    }
    
    public Node[] getRandomNonTerminalNode() {
        return null;
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

    public void printTree() {
        root.print();
    }
}
