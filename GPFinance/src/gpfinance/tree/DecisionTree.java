package gpfinance.tree;

import gpfinance.U;
import java.util.ArrayList;
import java.util.Random;

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
    private static final int NEXT = 2;

    public DecisionTree() {
        init();
    }

    public DecisionTree(char type) {
        this.type = type;
        init();
    }

    public DecisionTree(char type, int numNodes) {
        this.numNodes = numNodes;
        this.type = type;
        init();
    }

    private void init() {
        root = generateRandomNonterminalNode();

        for (int i = 0; i < numNodes; ++i) {
            insertRandom();
        }
    }

    private CriteriaNode generateRandomNonterminalNode() {
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
    
    public Node[] getRandomNonterminalNode(boolean depthLimited){
        // make list of non-terminal node pairs (prev, curr)
        ArrayList<Node[]> list = new ArrayList();
        if (!depthLimited){
            constructListOfNonTerminalPairsRec(list, root.left, root, (short)1);
            constructListOfNonTerminalPairsRec(list, root.right, root, (short)1);
        } else {
            constructListOfNonTerminalPairsLimitedDepthRec(list, root.left, root, (short)1);
            constructListOfNonTerminalPairsLimitedDepthRec(list, root.right, root, (short)1);
        }

        if (list.isEmpty()){
            return null;
        }
        int rand = new Random().nextInt(list.size());
        
        // choose random pair
        return list.get(rand);
    }
    
    private void constructListOfNonTerminalPairsRec(ArrayList<Node[]> list, Node next, Node prev, short depth){
        if (!next.isLeaf()){
            // Add current node to list
            Node[] nodes = new Node[2];
            nodes[PREV] = prev;
            nodes[CURR] = next;
            nodes[CURR].depth = depth;
            list.add(nodes);
            // Move along to add current nodes children
            constructListOfNonTerminalPairsRec(list, next.left, next, (short)(depth+1));
            constructListOfNonTerminalPairsRec(list, next.right, next,(short)(depth+1));
        }
    }
    
    private void constructListOfNonTerminalPairsLimitedDepthRec(ArrayList<Node[]> list, Node next, Node prev, short depth){
        if (!next.isLeaf()){
            // Add current node to list if next left and right are leaves (bottom most node)
            // nodes = {prev, node, next}
            if (next.left.isLeaf() && next.right.isLeaf()){
                Node[] nodes = new Node[3];
                nodes[PREV] = prev;
                nodes[CURR] = next;
                nodes[NEXT] = next.left;
                nodes[CURR].depth = depth;
                list.add(nodes);
            }
            // Move along to add current nodes children
            constructListOfNonTerminalPairsLimitedDepthRec(list, next.left, next, (short)(depth+1));
            constructListOfNonTerminalPairsLimitedDepthRec(list, next.right, next, (short)(depth+1));
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
    
    /**
     * Method finds a random terminal node, generates a non terminal node and replaces
     * the terminal with the non terminal.
     */
    public void insertRandom() {
        // Create new node
        CriteriaNode newNode = generateRandomNonterminalNode();

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
    
    /**
     * Method find a random non terminal node, generates a random decision node
     * and replaces the non terminal with the terminal - this is very destructive.
     */
    public void removeRandom() {
        Node[] nodes = getRandomNonterminalNode(false);
        if (nodes == null)
            return;
        
        // Generate random DecisionNode to replace
        DecisionNode replacementNode = DecisionNode.getRandom();
        
        // Update prev reference
        if (nodes[PREV].left == nodes[CURR]){
            nodes[PREV].left = replacementNode;
        } else {
            nodes[PREV].right = replacementNode;
        }
    }
    
    /**
     * Method performs the same function as removeRandom(), but is limited to remove
     * a non-terminal node that is a depth above a random terminal node.
     */
    public void removeRandomLimitedDepth(){
        Node[] nodes = getRandomNonterminalNode(true); //limited depth so suppress trunc's destructiveness.
        if (nodes == null)
            return;
        
        // Generate random DecisionNode to replace
        DecisionNode replacementNode = DecisionNode.getRandom();
        
        // Update prev reference
        if (nodes[PREV].left == nodes[CURR]){
            U.pl("Truncing: " + nodes[PREV] + ", " + nodes[CURR] + ", " + nodes[NEXT]);
            nodes[PREV].left = replacementNode;
        } else {
            U.pl("Truncing: " + nodes[PREV] + ", " + nodes[CURR] + ", " + nodes[NEXT]);
            nodes[PREV].right = replacementNode;
        }
    }

    public void gaussRandom() {
        Node[] nodes = getRandomNonterminalNode(false);
        if (nodes != null)
            ((CriteriaNode) nodes[1]).gaussValue();
    }

    public void swapRandomInequality() {
        Node[] nodes = getRandomNonterminalNode(false);
        if (nodes != null)
            ((CriteriaNode) nodes[1]).swapInequality();
    }

    public void mutateTerminalNode() {
        Node[] nodes = getRandomTerminalNode();
        ((DecisionNode) nodes[1]).swapDecision();
    }

    public void mutateNonterminalNode() {
        // swap out a non-terminal nodes indicator
        Node[] nodes = getRandomNonterminalNode(false);
        if (nodes != null)
            ((CriteriaNode) nodes[1]).randomizeIndicator();
    }
}
