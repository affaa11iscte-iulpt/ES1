package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {
	
	
	public static List<String> readRules(){
		List<String> rules = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(new File("rules.cf"));
			
			while(scanner.hasNextLine()) {
				rules.add(scanner.nextLine());
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error reading file rules.cf");
		}
		
		return rules;
	}
}
