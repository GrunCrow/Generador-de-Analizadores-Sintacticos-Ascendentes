package Proy;

import parserjj.*;


import java.io.*;

public class test {
	
	public static void main(String[] args) {
		
		
	    try {
	        Lexer lexer = new Lexer("Main.txt");
	        Token token;
	        while ((token = lexer.getNextToken()) != null) {
	            System.out.println("Token: " + token.kind + ", Valor: " + token.image);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}
