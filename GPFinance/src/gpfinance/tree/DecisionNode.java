
package gpfinance.tree;

import gpfinance.datatypes.Decision;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class DecisionNode extends Node {
    public Decision decision;
    
    public DecisionNode(){
        super(null, null);
        this.decision = Decision.BUY;
    }
    
    public DecisionNode(Decision decision){
        super(null, null);
        this.decision = decision;
    }
    
    public static DecisionNode getRandom(){
        return new DecisionNode(Decision.getRandom());
    }
    
    public void swapDecision(){
        if (decision == Decision.BUY){
            decision = Decision.SELL;
        } else {
            decision = Decision.BUY;
        }
    }
    
    @Override
    public boolean isLeaf(){
        return true;
    }
        
    @Override
    public String toString(){
        String tmp = decision.toString();
        int padd = 12 - tmp.length();
        for (int i = 0; i < padd; ++i)
            tmp += " ";
        return tmp;
    }
    
    @Override
    public DecisionNode clone(){
        return new DecisionNode(this.decision);
    }
}
