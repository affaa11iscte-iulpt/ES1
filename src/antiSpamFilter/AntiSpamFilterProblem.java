package antiSpamFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

public class AntiSpamFilterProblem extends AbstractDoubleProblem {

	  private Map<String, String> rules;
	
	  public AntiSpamFilterProblem(Map<String, String> rules) {
	    // 335 é o número total de regras que contém o ficheiro rules.cf
	    this(new Integer(rules.size()));
	    this.rules = rules;
	  }

	  public AntiSpamFilterProblem(Integer numberOfVariables) {
	    setNumberOfVariables(numberOfVariables);
	    setNumberOfObjectives(2);
	    setName("AntiSpamFilterProblem");

	    List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
	    List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

	    for (int i = 0; i < getNumberOfVariables(); i++) {
	      lowerLimit.add(-5.0);
	      upperLimit.add(5.0);
	    }

	    setLowerLimit(lowerLimit);
	    setUpperLimit(upperLimit);
	  }

	  public void evaluate(DoubleSolution solution){
	    double aux, xi, xj;
	    double[] falses = new double[getNumberOfObjectives()];
	    double[] values = new double[getNumberOfVariables()];
	    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
	      values[i] = solution.getVariableValue(i) ;
	    }
	    
	    Map<String, String> new_rules = new HashMap<String, String>();
	    
	    int j=0;
	    for(String key: this.rules.values()) {
	    	new_rules.put(key, String.valueOf(values[j]));
	    	j++;
	    }

	    //Falsos positivos
	    falses[0] = 0.0;
	    for (int var = 0; var < solution.getNumberOfVariables() - 1; var++) {
		  falses[0] += Math.abs(values[0]); // Example for testing
	    }
	    
	    //Falsos negativos
	    falses[1] = 0.0;
	    for (int var = 0; var < solution.getNumberOfVariables(); var++) {
	    	falses[1] += Math.abs(values[1]); // Example for testing
	    }

	    solution.setObjective(0, falses[0]);
	    solution.setObjective(1, falses[1]);
	  }
	  
	  
	  
	  
	}
