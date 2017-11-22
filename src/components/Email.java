package components;

import java.awt.List;
import java.util.ArrayList;

public class Email {
	private String id;
	private String type;
	private ArrayList<String> emailRules;
	
	public Email(String id, String type) {
		this.id = id;
		this.type = type;
		this.emailRules = new ArrayList<String>();
	}
	
	public String getId() {
		return id;
	}
	
	public String getEmailType() {
		return type;
	}
	
	public void addRule(String rule) {
		emailRules.add(rule);
	}
	
	public ArrayList<String> getEmailRules() {
		return emailRules;
	}
	

}
