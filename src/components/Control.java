package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import antiSpamFilter.AntiSpamFilterAutomaticConfiguration;
import components.Email.Type;

public class Control {
	private int fpos=0;
	private int fneg=0;

	/**
	 * Calcula o número de falsos positivos e falsos negativos
	 * @param Lista de emails e Mapa das regras
	 * @return falsos positivos e falsos negativos
	 */
	public void calculate (List<Email> emails, Map<String, String> rules) {		
		//System.out.println("N emails: "+emails.size());
		for (Email email : emails) {
			if(email.getEmailType().equals(Type.HAM)){
				if(isGreaterThan5(email, rules))
					fpos++;
			}
			if(email.getEmailType().equals(Type.SPAM))
				if(isLessThanMinus5(email, rules)) 
					fneg++;

		}
		//System.out.println(fpos+" "+fneg);
	}
	
	/**
	 * Devolve numero de falsos positivos
	 * @return Integer
	 */
	public int getFpos() {
		return fpos;
	}

	/**
	 * Devolve numero de falsos negativos
	 * @return Integer
	 */
	public int getFneg() {
		return fneg;
	}

	/**
	 * 
	 * @param email
	 * @param rules
	 * @return boolean
	 */

	private boolean isGreaterThan5(Email email, Map<String, String> rules) {
		int value=0;
		for(String rule: email.getEmailRules()) {
			value+= Integer.parseInt(rules.get(rule));
		}
		//System.out.println("MAIOR "+value);
		if(value > 5) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param email
	 * @param rules
	 * @return boolean
	 */
	private boolean isLessThanMinus5(Email email, Map<String, String> rules) {
		int value=0;
		int i=0;
		for(String rule: email.getEmailRules()) {
			//System.out.println(rule+" "+rules.get(rule));
			if(rules.get(rule)!=null)
				value += Integer.parseInt(rules.get(rule));
		}

		//System.out.println("MENOR "+value);
		if(value < -5) {
			return true;
		}
		return false;
	}

	/** L� o ficheiro rules.cf e guarda todas as regras num map <String, String> 
	 * em que o nome da regra corresponde � chave e o peso corresponde ao peso.
	 * Caso n�o esteja nenhum peso definido no ficheiro, este ficar� vazio.
	 * @param file Nome do ficheiro
	 * @return Lista de regras
	 */
	public static Map<String, String> readRules(String file){
		Map<String, String> rules = new HashMap<String, String>();
		try {
			Scanner scanner = new Scanner(new File(file));

			while(scanner.hasNextLine()) {
				String[] tokens = scanner.next().split(" ");
				if(tokens.length==1)
					rules.put(tokens[0], "");
				else
					rules.put(tokens[0], tokens[1]);
			}

		} catch (NullPointerException | FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("Error reading file rules.cf");
			return null;
		}

		return rules;
	}

	/**
	 * L� o ficheiro spam.log ou ham.log e guarda todos os emails lidos numa
	 * ArrayList<Email>. Caso o parametro isSpam assuma o valor true, � criado um
	 * email do tipo spam e adicionado � lista. Caso o parametro isSpam seja false,
	 * � criado um Email do tipo spam e adicionado � lista de emails. Ap�s a
	 * cria��o do Email s�o adicionadas a regras associadas ao mesmo.
	 * 
	 * @param file
	 * @param isSpam
	 * 
	 * @return Lista de emails
	 */
	public static List<Email> readEmails(String file, boolean isSpam) {
		List<Email> emails = new ArrayList<Email>();
		try {
			Scanner scanner  = new Scanner(new File(file));

			while(scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split("\t");
				Email email;
				if(isSpam == true) {
					email = new Email(tokens[0], Type.SPAM);
				} else {
					email = new Email(tokens[0], Type.HAM);
				}
				for(int i = 1; i<tokens.length; i++) {
					email.addRule(tokens[i]);
				}
				emails.add(email);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error reading " + file);
		}

		return emails;

	}

	public void modoAutomatico(List<Email> emails, Map<String, String> rules) {
		try {
			AntiSpamFilterAutomaticConfiguration.start(emails, rules);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
