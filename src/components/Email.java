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
	
	public Email(String id, Type type) {
		this.id = id;
		this.type = type;
		this.emailRules = new ArrayList<String>();
	}
	
	public String getId() {
		return id;
	}
	
	public Type getEmailType() {
		return type;
	}
	
	public void addRule(String rule) {
		emailRules.add(rule);
	}
	
	public ArrayList<String> getEmailRules() {
		return emailRules;
	}
	

}
