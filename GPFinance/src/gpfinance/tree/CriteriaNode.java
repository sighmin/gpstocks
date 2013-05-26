package gpfinance.tree;

import gpfinance.U;
import gpfinance.datatypes.Decision;
import gpfinance.datatypes.Fund;
import gpfinance.datatypes.Indicator;
import gpfinance.datatypes.Tech;

/**
 * @date 2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class CriteriaNode extends Node {

    public Indicator indicator;
    public char inequality;
    public double value;
    private char analysisType = 'F';

    public CriteriaNode(Node left, Node right, Indicator indicator, char inequality, double value) {
        super(left, right);
        this.indicator = indicator;
        this.inequality = inequality;
        this.value = value;
    }
    
    public CriteriaNode(Node left, Node right, Indicator indicator, char inequality, double value, char analysisType) {
        super(left, right);
        this.indicator = indicator;
        this.inequality = inequality;
        this.value = value;
        this.analysisType = analysisType;
    }

    public static CriteriaNode getRandomFundNode() {
        DecisionNode randomDecision = DecisionNode.getRandom();
        DecisionNode oppositeDecision = randomDecision.decision == Decision.BUY ? new DecisionNode(Decision.SELL) : new DecisionNode(Decision.BUY);
        return new CriteriaNode(
                randomDecision,
                oppositeDecision,
                Fund.getRandom(),
                U.chance() ? '<' : '>',
                U.randomVal());
    }

    public static CriteriaNode getRandomTechNode() {
        DecisionNode randomDecision = DecisionNode.getRandom();
        DecisionNode oppositeDecision = randomDecision.decision == Decision.BUY ? new DecisionNode(Decision.SELL) : new DecisionNode(Decision.BUY);
        return new CriteriaNode(
                randomDecision,
                oppositeDecision,
                Tech.getRandom(),
                U.chance() ? '<' : '>',
                U.randomVal());
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public String toString() {
        return indicator.toString() + " " + inequality + " " + Double.toString(value).substring(0, 6);
    }

    public void swapInequality() {
        if (inequality == '<'){
            inequality = '>';
        } else {
            inequality = '<';
        }
    }

    public void randomizeIndicator() {
        if (analysisType == 'F'){
            indicator = Fund.getRandom();
        } else {
            indicator = Tech.getRandom();
        }
    }
    
    public void gaussValue() {
        value = value + U.getRandomGauss();
    }
       
    @Override
    public Decision eval(double[] indicators){
        int index = indicator.getCode();
        if (inequality == '<'){
            if (indicators[index] < value){
                // go left
                return left.eval(indicators);
            } else {
                // go right
                return right.eval(indicators);
            }
        // indicator == '>'
        } else {
            if (indicators[index] > value){
                // go left
                return left.eval(indicators);
            } else {
                // go right
                return right.eval(indicators);
            }
        }
    }
    
    @Override
    public CriteriaNode clone(){
        return new CriteriaNode(this.left.clone(), this.right.clone(), this.indicator, this.inequality, this.value, this.analysisType);
    }
}
