package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Reader {
	
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
	
	public static void main(String[] args) {
		System.out.println(Reader.readRules("files/rules.cf").size());
	}
	
	
}
