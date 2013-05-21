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
    private static final int PREV = 0;
    private static final int CURR = 1;

    public DecisionTree() {
        init();
    }

    public DecisionTree(char type) {
        this.type = type;        
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

            nodes[PREV] = prev;
            nodes[CURR] = node;
        }

        return nodes;
    }
    
    public Node[] getRandomNonterminalNode(){
        // make list of non-terminal node pairs (prev, curr)
        ArrayList<Node[]> list = new ArrayList();
        constructListOfNonTerminalPairs(list);
        U.pl("Num non terminals: " + list.size());
        
        // choose random pair
        return list.get(new Random().nextInt(list.size()));
    }
    
    public void constructListOfNonTerminalPairs(ArrayList<Node[]> list){
        constructListOfNonTerminalPairsRec(list, root.left, root);
        constructListOfNonTerminalPairsRec(list, root.right, root);
    }
    
    private void constructListOfNonTerminalPairsRec(ArrayList<Node[]> list, Node next, Node prev){
        if (!next.isLeaf()){
            // Add current node to list
            Node[] nodes = new Node[2];
            nodes[PREV] = prev;
            nodes[CURR] = next;
            list.add(nodes);
            // Move along to add current nodes children
            constructListOfNonTerminalPairsRec(list, next.left, next);
            constructListOfNonTerminalPairsRec(list, next.right, next);
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
    
    public void insertRandom() {
        // Create new node
        CriteriaNode newNode = getRandomNode();

        // Get random leaf node
        Node[] nodes = getRandomTerminalNode();
        Node prev = nodes[PREV];
        Node node = nodes[CURR];

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

    public void removeRandom() {
        Node[] nodes = getRandomNonterminalNode();
        
        // Generate random DecisionNode to replace
        DecisionNode replacementNode = DecisionNode.getRandom();
        
        // Update prev reference
        if (nodes[PREV].left == nodes[CURR]){
            nodes[PREV].left = replacementNode;
        } else {
            nodes[PREV].right = replacementNode;
        }
    }

    public void gaussRandom() {
        Node[] nodes = getRandomNonterminalNode();
        ((CriteriaNode) nodes[1]).gaussValue();
    }

    public void swapRandomInequality() {
        Node[] nodes = getRandomNonterminalNode();
        ((CriteriaNode) nodes[1]).swapInequality();
    }

    public void mutateTerminalNode() {
        Node[] nodes = getRandomTerminalNode();
        ((DecisionNode) nodes[1]).swapDecision();
    }

    public void mutateNonterminalNode() {
        // swap out a non-terminal nodes indicator
        Node[] nodes = getRandomNonterminalNode();
        ((CriteriaNode) nodes[1]).randomizeIndicator();
    }
}
