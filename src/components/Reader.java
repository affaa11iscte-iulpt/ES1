package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.uma.jmetal.util.fileinput.util.ReadDoubleDataFile;

public class Reader {
	
	/**
	 * Lê o ficheiro rules.cf e guarda todas as regras numa lista.
	 * 
	 * @return Lista de regras
	 */
	public static List<String> readRules(String file){
		List<String> rules = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(new File(file));
			
			while(scanner.hasNextLine()) {
				rules.add(scanner.nextLine());
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
