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
	 * Calcula o nÃºmero de falsos positivos e falsos negativos
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
			if(rules.get(rule)!=null)
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

	/** Lï¿½ o ficheiro rules.cf e guarda todas as regras num map <String, String> 
	 * em que o nome da regra corresponde ï¿½ chave e o peso corresponde ao peso.
	 * Caso nï¿½o esteja nenhum peso definido no ficheiro, este ficarï¿½ vazio.
	 * @param file Nome do ficheiro
	 * @return Lista de regras
	 */
	public static Map<String, String> readRules(String file){
		Map<String, String> rules = new HashMap<String, String>();
		try {
			Scanner scanner = new Scanner(new File(file));

			while(scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split(" ");
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
	 * Lï¿½ o ficheiro spam.log ou ham.log e guarda todos os emails lidos numa
	 * ArrayList<Email>. Caso o parametro isSpam assuma o valor true, ï¿½ criado um
	 * email do tipo spam e adicionado ï¿½ lista. Caso o parametro isSpam seja false,
	 * ï¿½ criado um Email do tipo spam e adicionado ï¿½ lista de emails. Apï¿½s a
	 * criaï¿½ï¿½o do Email sï¿½o adicionadas a regras associadas ao mesmo.
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

		} catch (NullPointerException |FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("Error reading " + file);
			return null;
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
	
	public List<String> readAutomatic(String fileRF, String fileRS) {
		int best_position=-1; //Posição do melhor vetor
		int best_fneg=-1; //Valor do falso negativo mais baixo ---> PRIORITÁRIO pq é Professional
		int best_fpos=-1; //Valor do falso positivo mais baixo
		try {
			Scanner scanner = new Scanner(new File(fileRF));
			
			int line=1;
			while(scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split(" ");
				System.out.println(tokens[0]+" "+tokens[1]);
				int[] falses = {(int)Double.parseDouble(tokens[0]), (int)Double.parseDouble(tokens[1])};
				if(isBest(best_fneg, best_fpos, falses)) {
					best_position=line;
					best_fneg=falses[1];
					best_fpos=falses[0];
				}
				line++;
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error reading  "+ fileRF);
		}
		
		try {
			Scanner scanner = new Scanner(new File(fileRS));
			
			int line=1;
			while(scanner.hasNextLine()) {
				if(line==best_position) {
					String[] tokens = scanner.nextLine().split(" ");
					List<String> values = new ArrayList<String>();
					for(int i=0; i<tokens.length; i++)
						values.add(tokens[i]);
					fpos=best_fpos;
					fneg=best_fneg;
					return values;
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error reading  "+ fileRS);
		}
		
		return null;
	}
	
	public boolean isBest(int fneg, int fpos, int[] falses) {
		if(fneg==-1 || fpos==-1)
			return true;
		
		if(fneg >= falses[1])
			if(fpos >= falses[0])
				return true;
		return false;
	}
	
}
