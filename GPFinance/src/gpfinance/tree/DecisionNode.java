
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
}
