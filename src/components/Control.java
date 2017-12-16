package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

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
	 * percorre todas as regras e verifica se o seu peso Ã© maior que 5
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
	 * percorre todas as regras e verifica se o seu peso Ã© menor que -5
	 * @param email
	 * @param rules
	 * @return boolean
	 */
	private boolean isLessThanMinus5(Email email, Map<String, String> rules) {
		int value=0;
		for(String rule: email.getEmailRules()) {
			if(rules.get(rule)!=null)
				value += Integer.parseInt(rules.get(rule));
		}

		if(value < -5) {
			return true;
		}
		return false;
	}

	/** LÃª o ficheiro rules.cf e guarda todas as regras num map <String, String> 
	 * em que o nome da regra corresponde Ã  chave e o peso corresponde ao peso.
	 * Caso nÃ£o esteja nenhum peso definido no ficheiro, este ficarÃ¡ vazio.
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
			System.out.println("Error reading file rules.cf");
			return null;
		}

		return rules;
	}

	/**
	 * Lé o ficheiro spam.log ou ham.log e guarda todos os emails lidos numa
	 * ArrayList<Email>. Caso o parametro isSpam assuma o valor true, é criado um
	 * email do tipo spam e adicionado Ã  lista. Caso o parametro isSpam seja false,
	 * Ã© criado um Email do tipo spam e adicionado à lista de emails. Após a
	 * criação do Email são adicionadas a regras associadas ao mesmo.
	 * 
	 * @param String
	 * @param boolean
	 * 
	 * @return List<Email>
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
			System.out.println("Error reading " + file);
			return null;
		}

		return emails;

	}

	/**Função relativa ao modo automático
	 * 
	 * @param emails
	 * @param rules
	 */
	public void automaticMode(List<Email> emails, Map<String, String> rules) {
		try {
			AntiSpamFilterAutomaticConfiguration.start(emails, rules);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lé as regras e atribui valores aos falsos positivos e falsos negativos
	 * @param Map<String, String>
	 * @param String
	 * @param String
	 * @returnMap<String, String>
	 */
	public Map<String, String> readAutomatic(Map<String, String> rules, String fileRF, String fileRS) {
		int best_position=-1; //PosiÃ§Ã£o do melhor vetor
		int best_fneg=-1; //Valor do falso negativo mais baixo ---> PRIORITÃ�RIO porque Ã© Professional
		int best_fpos=-1; //Valor do falso positivo mais baixo
		try {
			Scanner scanner = new Scanner(new File(fileRF));

			int line=1;
			while(scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split(" ");
				//System.out.println(tokens[0]+" "+tokens[1]);
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
			return null;
		}

		List<Integer> values = null;

		try {
			Scanner scanner = new Scanner(new File(fileRS));

			int line=1;
			while(scanner.hasNextLine()) {
				if(line==best_position) {
					String[] tokens = scanner.nextLine().split(" ");
					values = new ArrayList<Integer>();
					for(int i=0; i<tokens.length; i++)
						values.add((int)Double.parseDouble(tokens[i]));
					fpos=best_fpos;
					fneg=best_fneg;
				}else
					scanner.nextLine();
				line++;
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error reading  "+ fileRS);
			return null;
		}
		return changeListToMap(rules, values);
	}

	/**
	 * Função para alterar uma lista para um mapa
	 * @param Map<String, String>
	 * @param List<Integer>
	 * @return Map<String, String>
	 */
	public Map<String, String> changeListToMap(Map<String, String> rules, List<Integer> values){
		Map<String, String> new_rules = new HashMap<String, String>(); 
		int j=0;
		for(String key: rules.keySet()) {
			new_rules.put(key, String.valueOf(values.get(j)));
			j++;
		}
		return new_rules;
	}

	/**
	 * Função que compara dois valores inteiros com os valores do vetor e verifica se são maiores ou não.
	 * @param int
	 * @param int
	 * @param int[]
	 * @return boolean
	 */
	public boolean isBest(int fneg, int fpos, int[] falses) {
		if(fneg==-1 || fpos==-1)
			return true;

		if(fneg >= falses[1])
			if(fpos >= falses[0])
				return true;
		return false;
	}

	/**
	 * Função para guardar as configurações do ficheiro rules.cf
	 * 
	 * @param String 
	 * @param Map<String, String>
	 */
	public void saveConfigurations(String file, Map<String, String> rules) {
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(file, false));
			for(String rule: rules.keySet()) {
				pw.println(rule+" "+rules.get(rule));
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao guardar configuração.");
		}
	}

	/**
	 * Função para colocar todos os pesos das regras do ficheiro rules.cf com o valor 0 e depois guardar a nova configuração.
	 * 
	 */
	public void removeRowFile(){
		List<String> rules = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(new File("files/rules.cf"));

			int line=1;
			while(scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split(" ");
				rules.add(tokens[0]);

				line++;
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error reading  rules.cf ");
		}

		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream("files/rules.cf", false));
			for(String rule: rules)
				pw.println(rule);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao guardar configuraÃ§Ã£o.");
		}
	}

}


