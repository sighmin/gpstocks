
package gpfinance.algorithm;

import java.util.ArrayList;

/**
 * @date   2013-06-01
 * @author Simon van Dyk, Stuart Reid
 */
public class InitializationStrategy {
    
    private char analysisType;

    public InitializationStrategy(char analysisType) {
        this.analysisType = analysisType;
    }
    
    public void init(ArrayList<Individual> population, int size){
        for (int i = 0; i < size; ++i){
            population.add(new Individual(analysisType));
        }
    }
    
    @Override
    public InitializationStrategy clone(){
        return new InitializationStrategy(this.analysisType);
    }
}
