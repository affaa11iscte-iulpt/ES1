package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Control {
	
	
	
	
	
	
	
	
	/**
	 * Lê o ficheiro rules.cf e guarda todas as regras num map <String, String> 
	 * em que o nome da regra corresponde à chave e o peso corresponde ao peso.
	 * Caso não esteja nenhum peso definido no ficheiro, este ficará vazio.
	 * 
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
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error reading file rules.cf");
		}
		
		return rules;
	}
	
	/**
	 * Lê o ficheiro spam.log ou ham.log e guarda todos os emails lidos numa
	 * ArrayList<Email>. Caso o parametro isSpam assuma o valor true, é criado um
	 * email do tipo spam e adicionado à lista. Caso o parametro isSpam seja false,
	 * é criado um Email do tipo spam e adicionado à lista de emails. Após a
	 * criação do Email são adicionadas a regras associadas ao mesmo.
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
				String[] tokens = scanner.next().split(" ");
				Email email;
				if(isSpam == true) {
					email = new Email(tokens[0], "spam");
				} else {
					email = new Email(tokens[0], "ham");
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
	
	public static void main(String[] args) {
		System.out.println(Control.readRules("files/rules.cf").size());
		System.out.println(Control.readEmails("files/spam.log", true).size());
		System.out.println(Control.readEmails("files/ham.log", false).size());
	}
	
	
}
