public interface TokenConstants {

	 public int EOF = 0;
	 public int PLUS = 1;
	 public int MINUS = 2;
	 public int PROD = 3;
	 public int DIV = 4;
	 public int NUM = 5;
	 public int LPAREN = 6;
	 public int RPAREN = 7;
	 public int IDENTIFIER = 8;
	 public int COMMA = 9;

}
Expr ->[Term ]
Expr ->[Expr , <PLUS> , Term ]
Expr ->[Expr , <MINUS> , Term ]
Term ->[Factor ]
Term ->[Term , <PROD> , Factor ]
Term ->[Term , <DIV> , Factor ]
Factor ->[<NUM> ]
Factor ->[<LPAREN> , Expr , <RPAREN> ]
Factor ->[<IDENTIFIER> , <LPAREN> , Args , <RPAREN> ]
Args ->[ArgumentList ]
Args ->[lambda ]
ArgumentList ->[Expr ]
ArgumentList ->[ArgumentList , <COMMA> , Expr ]

CONJUNTO PRIMEROS
Expr ->[Term ]PRIMEROS([<NUM>, <LPAREN>, <IDENTIFIER>])
Term ->[Factor ]PRIMEROS([<NUM>, <LPAREN>, <IDENTIFIER>])
Factor ->[<NUM> ]PRIMEROS([<NUM>, <LPAREN>, <IDENTIFIER>])
Args ->[ArgumentList ]PRIMEROS([<NUM>, <LPAREN>, <IDENTIFIER>, lambda])
ArgumentList ->[Expr ]PRIMEROS([<NUM>, <LPAREN>, <IDENTIFIER>])

CONJUNTO SIGUIENTES
Expr ->[Term ]SIGUIENTES([<EOF>, <PLUS>, <MINUS>, <RPAREN>, <COMMA>])
Term ->[Factor ]SIGUIENTES([<EOF>, <PLUS>, <MINUS>, <RPAREN>, <COMMA>, <PROD>, <DIV>])
Factor ->[<NUM> ]SIGUIENTES([<EOF>, <PLUS>, <MINUS>, <RPAREN>, <COMMA>, <PROD>, <DIV>])
Args ->[ArgumentList ]SIGUIENTES([<RPAREN>])
ArgumentList ->[Expr ]SIGUIENTES([<RPAREN>, <COMMA>])
