package components;

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
 */

/**
 * Controi um novo Control que permite o cálculo de falsos negativos e
 * falsos positivos, leitura e a escrita nos respetivos ficheiros auxiliares,
 * quer no modo automático, quer no modo manual.
 *
 */
@SuppressWarnings({ "resource", "unused" })
public class Control {
	private int fpos=0;
	private int fneg=0;
	

	/**
	 * Calcula o numero de falsos positivos e o numero de falsos negativos
	 * guardado-os nos atributos da classe fpos e fneg.
	 * 
	 * Este metodo percorre a lista de emails e dependendo se esses emails
	 * foram do tipo HAM ou do tipo SPAM, vai calcular o numero de falsos
	 * positivos e o numero de falsos negativos consoante os pesos que estao
	 * atribuidos as respetivas regras no HashMap de regras.
	 * 
	 * @param emails uma lista de emails
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
	 */
	public void calculate (List<Email> emails, Map<String, String> rules) {		
		for (Email email : emails) {
			if(email.getEmailType().equals(Type.HAM)){
				if(isGreaterThan5(email, rules))
					fpos++;
			}
			if(email.getEmailType().equals(Type.SPAM)) {
				if(isLessThan5(email, rules)) 
					fneg++;
			}
		}
		System.out.println("tamanho da lista de emails" + emails.size());
	}
	
	/**
	 * Faz reset do numero de falsos negativos da classe Control
	 * fneg passa a assumir o valor 0
	 */
	public void resetFneg() {
		this.fneg = 0;
	}
	
	/**
	 * Faz reset do numero de falsos positivos da classe Control
	 * fpos passa a assumir o valor 0
	 */
	public void resetFpos() {
		this.fpos = 0;
	}
	
	/**
	 * Devolve o numero de falsos positivos
	 * @return int numero de falsos positivos
	 */
	public int getFpos() {
		return fpos;
	}

	/**
	 * Devolve o numero de falsos negativos
	 * @return int numero de falsos negativos
	 */
	public int getFneg() {
		return fneg;
	}

	/**
	 * Devolve verdadeiro caso o total dos pesos correspondente a um dado
	 * email seja superior a 5 e falso caso seja menor.
	 * 
	 * Esta funcao percorre a lista de regras de um email e, para cada uma
	 * dessas regras, vai obter o peso respetivo que se encontra no Map
	 * das regras. Esse peso e acumulativo.
	 * 
	 * @param email que sera usado
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
	 * @return true se o total de pesos for maior que 5; falso caso contrario 
	 */
	private boolean isGreaterThan5(Email email, Map<String, String> rules) {
		int value=0;
		for(String rule: email.getEmailRules()) {
			if(rules.get(rule)!=null)
				value+= Integer.parseInt(rules.get(rule));
		}
		if(value > 5) {
			return true;
		}
		return false;
	}

	/**
	 * Devolve verdadeiro caso o total dos pesos correspondente a um dado
	 * email seja inferior a -5 e falso caso seja maior.
	 * 
	 * Esta funcao percorre a lista de regras de um email e, para cada uma
	 * dessas regras, vai obter o peso respetivo que se encontra no Map
	 * das regras. Esse peso e cumulativo.
	 * 
	 * @param email que sera usado
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
	 * @return true se o total de pesos for menor que -5; falso caso contrario 
	 */
	private boolean isLessThan5(Email email, Map<String, String> rules) {
		int value=0;
		for(String rule: email.getEmailRules()) {
			if(rules.get(rule)!=null)
				value += Integer.parseInt(rules.get(rule));
		}
		if(value < 5) {
			return true;
		}
		return false;
	}

	/** Permite ler um ficheiro de regras da estrutura "REGRAS PESO"
	 * e guarda as informacoes num Map em que a chave e a regra
	 * e o valor e o peso.
	 * 
	 * A funcao começa por ler o ficheiro e percorrer as suas linhas
	 * ate nao existirem mais. Por cada linha que exista vai separar
	 * a regra e o peso e guardar num Map. Caso o ficheiro nao possua
	 * um peso atribuido a uma regra, este fica em branco.
	 * 
	 * @param file de regras para ler
	 * @return rules um mapa que tem como chave a regra 
	 * e como valor o seu peso
	 */
	public static Map<String, String> readRules(String file){
		Map<String, String> rules = new HashMap<String, String>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(file));

			while(scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split(" ");
				if(tokens.length==1)
					rules.put(tokens[0], "");
				else
					rules.put(tokens[0], tokens[1]);
			}
			scanner.close();

		} catch (NullPointerException | FileNotFoundException e) {
			System.out.println("Error reading "+file);
			JOptionPane.showMessageDialog(null, "Ficheiro "+file+" não existe.");
			return null;
		}

		return rules;
	}

	/**
	 * Le o ficheiro spam.log ou ham.log e guarda todos os emails lidos numa
	 * ArrayList. Caso o parametro isSpam assuma o valor true, e criado um
	 * email do tipo spam e adicionado a lista. Caso o parametro isSpam seja false,
	 * cria-se um Email do tipo spam e adicionado a lista de emails. Apos a
	 * criacao do Email sao adicionadas a regras associadas ao mesmo.
	 * 
	 * @param file ficheiro com as regras
	 * @param isSpam booleano que indica se o ficheiro é do tipo spam; falso 
	 * caso contrário
	 * 
	 * @return emails lista de emails
	 */
	public static List<Email> readEmails(String file, boolean isSpam) {
		List<Email> emails = new ArrayList<Email>();
		Scanner scanner = null;
		try {
			scanner  = new Scanner(new File(file));

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
			scanner.close();

		} catch (NullPointerException |FileNotFoundException e) {
			System.out.println("Error reading " + file);
			JOptionPane.showMessageDialog(null, "Ficheiro "+file+" não existe.");
			return null;
		} 

		return emails;

	}

	/**Inicia o filtro AntiSpamFilter no modo automatico.
	 * 
	 * @param emails lista de emails que irao servir de base no calculo
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
	 * @throws Exception quando ocorre um erro
	 */
	public void automaticMode(List<Email> emails, Map<String, String> rules) throws Exception{
		AntiSpamFilterAutomaticConfiguration.start(emails, rules);
	}

	/**
	 * Permite calcular, com base num mapa de regras, qual o vetor de pesos o mais
	 * adequado para o uso profissional com base nos falsos positivos e falsos
	 * negativos de cada.
	 * 
	 * A função lê o ficheiro de tipo .rf e obtém os valores dos falsos positivos
	 * e falsos negativos respetivamente, guardando-os num vetor. Para cada vetor,
	 * é verificado se os valores do vetor são mais adequados em comparação aos
	 * valores anteriormente armazenados. Caso o sejam, estes são substituidos pelos
	 * valores do vetor.
	 * 
	 * Após a descoberta dos melhores falsos positivos e falsos negativos, procede-se
	 * à leitura do ficheiro do tipo RS, onde estão armazenados os vários pesos, até se
	 * chegar à linha do ficheiro correspondente à dos melhores falsos negativos e
	 * falsos positivos. Deste modo, os pesos são armazenados num Map em que a chave
	 * é a regra e o valor é o peso.
	 * 
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
	 * @param fileRF ficheiro do tipo .rf
	 * @param fileRS ficheiro do tipo .rs
	 * @return mapRules um mapa que tem como chave a regra e como valor o seu peso
	 */
	public Map<String, String> readAutomatic(Map<String, String> rules, String fileRF, String fileRS) {
		int best_position=-1; //Posicao do melhor vetor
		int best_fneg=-1; //Valor do falso negativo mais baixo ---> PRIORITARIO porque e Professional
		int best_fpos=-1; //Valor do falso positivo mais baixo
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(fileRF));

			int line=1;
			while(scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split(" ");
				int[] falses = {(int)Double.parseDouble(tokens[0]), (int)Double.parseDouble(tokens[1])};
				if(isBest(best_fneg, best_fpos, falses)) {
					best_position=line;
					best_fneg=falses[1];
					best_fpos=falses[0];
				}
				line++;
			}
			scanner.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error reading  "+ fileRF);
			JOptionPane.showMessageDialog(null, "Ficheiro "+fileRF+" não existe.");
		}

		List<Integer> values = null;

		if(scanner!=null) {
			try {
				scanner = new Scanner(new File(fileRS));

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
				scanner.close();
			}catch(FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Error reading  "+ fileRS);
				JOptionPane.showMessageDialog(null, "Ficheiro "+fileRS+" não existe.");

				return null;
			}
		}else {
			return null;
		}
		return changeListToMap(rules, values);
	}

	/**
	 * Permite agrupar as regras que estão num Map com os pesos que proveem da lista
	 * de pesos, devolvendo um novo Map com as regras e os novos pesos.
	 * 
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
	 * @param values uma lista de pesos
	 * @return new_rules um mapa que tem como chave a regra 
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
	 * que os que estao nas restantes variaveis, respetivamente, tendo em
	 * conta que sera dada uma prioridade aos falsos negativos.
	 * 
	 * Devido a prioridade dada aos falsos negativos, sera primeiro analisado
	 * se o novo falso negativo, que esta na segunda posição do vetor, e menor
	 * que a variavel fneg. Caso seja verdadeiro, ira verificar se o falso
	 * positivo do vetor e menor que a variável fpos. Caso isto tudo seja devolve
	 * true. Caso contrario false.
	 * 
	 * Caso os valores iniciais do fneg ou do fpos sejam -1, devolve true.
	 * 
	 * @param fneg falsos negativos
	 * @param fpos falsos positivos
	 * @param falses com os falsos positivos e falsos negativos, respetivamente
	 * @return true caso os valores do vetor sejam mais adequados; false caso
	 * contrario
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
	 * Guarda as configuracoes das regras provenientes do mapa, em que a chave
	 * e a regra e o valor e o peso, num dado ficheiro file.
	 * 
	 * Percorre o mapa todo e, para cada peso, obtem o peso correspondente e
	 * escreve no ficheiro da forma "REGRA PESO".
	 * 
	 * @param file nome do ficheiro
	 * @param rules um mapa que tem como chave a regra e como valor o seu peso
	 * @return true se conseguiu guardar com sucesso; false caso contrário
	 */
	public boolean saveConfigurations(String file, Map<String, String> rules) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(file, false));
			for(String rule: rules.keySet()) {
				pw.println(rule+" "+rules.get(rule));
			}
			pw.close();
		} catch (NullPointerException | FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao guardar configuração.");
			return false;
		}
		return true;
	}

	/**
	 * Limpa os pesos das regras de um determinado ficheiro
	 * 
	 * Ocorre a leitura do ficheiro file e as regras são armazenadas numa
	 * lista de regras. Apos a leitura, ocorre a escrita no mesmo ficheiro
	 * que segue o formato "REGRA", não escrevendo os pesos que tinha
	 * anteriormente.
	 * 
	 *@param file ficheiro de regras
	 *@return true se removeu com sucesso; false caso contrário
	 */
	public boolean removeRowFile(String file){
		List<String> rules = new ArrayList<String>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(file));

			int line=1;
			while(scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split(" ");
				rules.add(tokens[0]);

				line++;
			}
			scanner.close();
		}catch(NullPointerException | FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error reading "+file);
			JOptionPane.showMessageDialog(null, "Ficheiro "+file+" não existe.");
			return false;
		}

		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(file, false));
			for(String rule: rules)
				pw.println(rule);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao guardar configuraÃ§Ã£o.");
			return false;
		}
		return true;
	}

}


