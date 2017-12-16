package components;

import java.util.ArrayList;

/*
 * FALTA
 * Melhorar javadoc
 */
public class Email {
	private String id;
	private Type type;
	private ArrayList<String> emailRules;

	/**
	 * Type{ HAM, SPAM; }
	 * 
	 * Enumerado que define o tipo de email (HAM ou SPAM)
	 *
	 */
	public enum Type {
		HAM, SPAM;
	}

	/**
	 * public Email(String id, Type type)
	 * 
	 * Constroi um novo Email com um id unico e um tipo associado que podera ser HAM ou SPAM.
	 * 
	 * @param id - identificacao do email
	 * @param type - tipo de email
	 */
	public Email(String id, Type type) {
		this.id = id;
		this.type = type;
		this.emailRules = new ArrayList<String>();
	}

	/**
	 * public String getId()
	 * 
	 * Retorna o id do Email
	 * 
	 * @return id - identificacao do email
	 */
	public String getId() {
		return id;
	}

	/**
	 * public Type getEmailType()
	 * 
	 * Retorna o tipo do email
	 * 
	 * @return type - tipo de email
	 */
	public Type getEmailType() {
		return type;
	}

	/**
	 * public void addRule(String rule)
	 * 
	 * Adiciona uma regra a lista de regras associadas ao email
	 * 
	 * @param rule - regra que se pretende adicionar a lista
	 */
	public void addRule(String rule) {
		if(rule != null)
			emailRules.add(rule);
	}

	/**
	 * public ArrayList getEmailRules()
	 * 
	 * Retorna a lista de regras associadas ao email
	 * 
	 * @return emailRules - lista de regras
	 */
	public ArrayList<String> getEmailRules() {
		return emailRules;
	}
	
	@Override
	public String toString() {
		return "Email [ id=" + id + "; tipo=" + type + " ]";  
	}

}