package components;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

import antiSpamFilter.AntiSpamFilterAutomaticConfiguration;
import components.Email.Type;

/*
 * FALTA
 * JUnit do automaticMode (????)
 * JUnit do saveConfiguration (????)
 * Melhorar javadoc
 */

@SuppressWarnings({ "resource", "unused" })
public class Control {
	private int fpos=0;
	private int fneg=0;

	/**
	 * Calcula o número de falsos positivos e o número de falsos negativos
	 * guardado-os nos atributos da classe fpos e fneg.
	 * 
	 * Este método percorre a lista de emails e dependendo se esses emails
	 * foram do tipo HAM ou do tipo SPAM, vai calcular o número de falsos
	 * positivos e o número de falsos negativos consoante os pesos que estão
	 * atribuidos às respetivas regras no HashMap de regras.
	 * 
	 * @param emails uma lista de emails
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
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
	 * Devolve o número de falsos positivos
	 * @return int número de falsos positivos
	 */
	public int getFpos() {
		return fpos;
	}

	/**
	 * Devolve o número de falsos negativos
	 * @return int número de falsos negativos
	 */
	public int getFneg() {
		return fneg;
	}

	/**
	 * Devolve verdadeiro caso o total dos pesos correspondente a um dado
	 * email seja superior a 5 e falso caso seja menor.
	 * 
	 * Esta função percorre a lista de regras de um email e, para cada uma
	 * dessas regras, vai obter o peso respetivo que se encontra no Map
	 * das regras. Esse peso é acumulativo.
	 * 
	 * @param email que será usado
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
	 * @return true se o total de pesos for maior que 5; falso caso contrário 
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
	 * Devolve verdadeiro caso o total dos pesos correspondente a um dado
	 * email seja inferior a -5 e falso caso seja maior.
	 * 
	 * Esta função percorre a lista de regras de um email e, para cada uma
	 * dessas regras, vai obter o peso respetivo que se encontra no Map
	 * das regras. Esse peso é cumulativo.
	 * 
	 * @param email que será usado
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
	 * @return true se o total de pesos for menor que -5; falso caso contrário 
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

	/** Permite ler um ficheiro de regras da estrutura "REGRAS PESO"
	 * e guarda as informações num Map em que a chave é a regra
	 * e o valor é o peso.
	 * 
	 * A função começa por ler o ficheiro e percorrer as suas linhas
	 * até não existirem mais. Por cada linha que exista irá separar
	 * a regra e o peso e guardar num Map. Caso o ficheiro não possua
	 * um peso atribuido a uma regra, este ficará em branco.
	 * 
	 * @param file de regras para ler
	 * @return Map<String, String> um mapa que tem como chave a regra 
	 * e como valor o seu peso
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

		} catch (FileNotFoundException e) {
			System.out.println("Error reading "+file);
			JOptionPane.showMessageDialog(null, "Ficheiro "+file+" não existe.");
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
			JOptionPane.showMessageDialog(null, "Ficheiro "+file+" não existe.");
			return null;
		}

		return emails;

	}

	/**Inicia o filtro AntiSpamFilter no modo automático.
	 * 
	 * @param emails lista de emails que irão servir de base no cálculo
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
	 */
	public void automaticMode(List<Email> emails, Map<String, String> rules) {
		try {
			AntiSpamFilterAutomaticConfiguration.start(emails, rules);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Ocorreu um erro. Tente novamente.");
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
			JOptionPane.showMessageDialog(null, "Ficheiro "+fileRF+" não existe.");
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
			JOptionPane.showMessageDialog(null, "Ficheiro "+fileRS+" não existe.");

			return null;
		}
		return changeListToMap(rules, values);
	}

	/**
	 * Permite agrupar as regras que estão num Map com os pesos que proveem da lista de pesos,
	 * devolvendo um novo Map com as regras e os novos pesos.
	 * 
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
	 * @param values uma lista de pesos
	 * @return Map<String, String> um mapa que tem como chave a regra 
	 * e como valor o seu peso
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
	 * Devolve verdadeiro caso os pesos provenientes do vetor sejam melhores
	 * que os que estão nas restantes variáveis, respetivamente, tendo em
	 * conta que será dada uma prioridade aos falsos negativos.
	 * 
	 * Devido à prioridade dada aos falsos negativos, será primeiro analisado
	 * se o novo falso negativo, que está na segunda posição do vetor, é menor
	 * que a variável fneg. Caso seja verdadeiro, irá verificar se o falso
	 * positivo do vetor é menor que a variável fpos. Caso isto tudo seja devolve
	 * true. Caso contrário false.
	 * 
	 * Caso os valores iniciais do fneg ou do fpos sejam -1, devolve true.
	 * 
	 * @param int falsos negativos
	 * @param int falsos positivos
	 * @param int[] com os falsos positivos e falsos negativos, respetivamente
	 * @return true caso os valores do vetor sejam mais adequados; false caso
	 * contrário
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
	 * Guarda as configurações das regras provenientes do mapa, em que a chave
	 * é a regra e o valor é o peso, num dado ficheiro file.
	 * 
	 * Percorre o mapa todo e, para cada peso, obtém o peso correspondente e
	 * escreve no ficheiro da forma "REGRA PESO".
	 * 
	 * @param file nome do ficheiro
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
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
	public void removeRowFile(String file){
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
			System.out.println("Error reading "+file);
			JOptionPane.showMessageDialog(null, "Ficheiro "+file+" não existe.");
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


