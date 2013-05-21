package gpfinance.tree;

import gpfinance.U;
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
        return new CriteriaNode(
                DecisionNode.getRandom(),
                DecisionNode.getRandom(),
                Fund.getRandom(),
                U.chance() ? '<' : '>',
                U.randomVal());
    }

    public static CriteriaNode getRandomTechNode() {
        return new CriteriaNode(
                DecisionNode.getRandom(),
                DecisionNode.getRandom(),
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
}
