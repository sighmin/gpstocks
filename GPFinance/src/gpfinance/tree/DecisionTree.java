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
        Node[] nodes = getRandomLeaf();
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

    public Node[] getRandomLeaf() {
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

    private int size(Node node) {
        int size = 0;
        if (node != null) {
            size += size(node.left);
            ++size;
            size += size(node.right);
        }
        return size;
    }
    
    public void displaySmallTree() {
        Stack globalStack = new Stack();
        globalStack.push(root);
        int emptyLeaf = 32;
        boolean isRowEmpty = false;
        while (isRowEmpty == false) {

            Stack localStack = new Stack();
            isRowEmpty = true;
            for (int j = 0; j < emptyLeaf; j++) {
                System.out.print(' ');
            }
            while (globalStack.isEmpty() == false) {
                Node temp = (Node) globalStack.pop();
                if (temp != null) {
                    System.out.print(temp);
                    localStack.push(temp.left);
                    localStack.push(temp.right);
                    if (temp.left != null || temp.right != null) {
                        isRowEmpty = false;
                    }
                } else {
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int j = 0; j < emptyLeaf * 2 - 2; j++) {
                    System.out.print(' ');
                }
            }
            System.out.println();
            emptyLeaf /= 2;
            while (localStack.isEmpty() == false) {
                globalStack.push(localStack.pop());
            }
        }
    }

    public void displayLargeTree() {
        root.print();
    }
}
