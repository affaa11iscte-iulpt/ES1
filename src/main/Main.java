package main;

import java.io.File;
import java.io.IOException;

import Interface.Interface;

public class Main {

	public static void main (String[] args){
		Interface t = new Interface();
		t.open();
	
	String[] params = new String[2];
	String[] envp = new String[1];

	params[0] = "C:\\Program Files\\R\\R-3.4.3\\bin\\x64\\Rscript.exe";
	params[1] = "C:\\Users\\Carol\\git\\ES1-2017-METIA-46-B\\experimentBaseDirectory\\AntiSpamStudy\\R\\HV.Boxplot.R";
	envp[0] = "Path = C:\\Program Files\\R\\R-3.4.3\\bin\\x64";

	try {
		Process p = Runtime.getRuntime().exec(params,envp, new File("C:\\Users\\Carol\\git\\ES1-2017-METIA-46-B\\experimentBaseDirectory\\AntiSpamStudy\\R"));
	} catch (IOException e) {
		System.out.println("Erro a gerar os gráficos R");
	}


	String[] paramsLatex = new String[2];
	String[] envpLatex = new String[1];

	paramsLatex[0] = "C:\\Users\\Carol\\AppData\\Local\\Programs\\MiKTeX 2.9\\miktex\\bin\\x64\\pdflatex.exe";
	paramsLatex[1] = "C:\\Users\\Carol\\git\\ES1-2017-METIA-46-B\\experimentBaseDirectory\\AntiSpamStudy\\latex\\AntiSpamStudy.tex";
	envpLatex[0] = "Path = C:\\Program Files\\R\\R-3.4.3\\bin\\x64";

		try {
			Process p = Runtime.getRuntime().exec(paramsLatex,envpLatex, new File("C:\\Users\\Carol\\git\\ES1-2017-METIA-46-B\\experimentBaseDirectory\\AntiSpamStudy\\latex"));
		} catch (IOException e) {
			System.out.println("Erros nos ficheiros latex");
		}
	

}

}
