package antiSpamFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import components.Control;
import components.Email;

public class AntiSpamFilterProblem extends AbstractDoubleProblem {
	  private List<Email> emails;
	  private Map<String, String> rules;
	
	  public AntiSpamFilterProblem(List<Email> emails, Map<String, String> rules) {
	    // 335 � o n�mero total de regras que cont�m o ficheiro rules.cf
	    this(new Integer(rules.size()));
	    this.rules = rules;
	    this.emails = emails;
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

	  /**
	   * Calcula o n�mero de falsos positivos e de falsos negativos 
	   * @param DoubleSolution Vetor de doubles
	   */
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

	    Control control = new Control();
	    control.calculate(emails, new_rules);
	    //Falsos positivos
	    falses[0] = control.getFpos();
	    
	    //Falsos negativos
	    falses[1] = control.getFneg();
	   
	    solution.setObjective(0, falses[0]);
	    solution.setObjective(1, falses[1]);
	  }
	  
	  
	  
	  
	}
