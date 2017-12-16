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
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Email> emails;
	  private Map<String, String> rules;
	
	  public AntiSpamFilterProblem(List<Email> emails, Map<String, String> rules) {
	    // 335 é o número total de regras que contém o ficheiro rules.cf
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
	   * Calcula o número de falsos positivos e de falsos negativos do modo au
	   * 
	   * @param DoubleSolution Vetor de pesos
	   */
	  public void evaluate(DoubleSolution solution){
		//System.out.println("Tratando");
	    double[] falses = new double[getNumberOfObjectives()];
	    double[] values = new double[getNumberOfVariables()];
	    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
	      values[i] = solution.getVariableValue(i) ;
	    }
	    
	    Map<String, String> new_rules = new HashMap<String, String>();
	    
	    int j=0;
	    for(String key: this.rules.keySet()) {
	    	new_rules.put(key, String.valueOf((int)values[j]));
	    	j++;
	    }
	    //System.out.println(new_rules);
	    Control control = new Control();
	    control.calculate(emails, new_rules);
	    //System.out.println("acabou");
	    //Falsos positivos
	    falses[0] = control.getFpos();
	    
	    //Falsos negativos
	    falses[1] = control.getFneg();
	   
	    solution.setObjective(0, falses[0]);
	    solution.setObjective(1, falses[1]);
	  }
	  
	  
	  
	  
	}
