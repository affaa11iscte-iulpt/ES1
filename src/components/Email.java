package components;

import java.awt.List;
import java.util.ArrayList;

public class Email {
	private String id;
	private Type type;
	private ArrayList<String> emailRules;

	public enum Type {
		HAM, SPAM;
	}
	
	/**
	 * Contrutor da classe Email que recebe um id e um typo (HAM ou SPAM)
	 * @param id
	 * @param type
	 */
	public Email(String id, Type type) {
		this.id = id;
		this.type = type;
		this.emailRules = new ArrayList<String>();
	}
	
	/**
	 * Getter do ID do email
	 * @return id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Getter do tipo do email
	 * @return type
	 */
	public Type getEmailType() {
		return type;
	}
	
	/**
	 * Associacao de uma regra a um email. Adicao da regra a lista de regras do email
	 * @param rule
	 */
	public void addRule(String rule) {
		if(rule != null)
			emailRules.add(rule);
	}
	
	/**
	 * Getter da lista de regras associadas ao email
	 * @return
	 */
	public ArrayList<String> getEmailRules() {
		return emailRules;
	}
	

}
