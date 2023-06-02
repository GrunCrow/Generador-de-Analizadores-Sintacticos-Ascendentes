package generated;

import auxiliares.ActionElement;
import auxiliares.SLRParser;


public class Parser extends SLRParser implements TokenConstants, SymbolConstants {

 	public Parser() {
		initRules();
		initActionTable();
		initGotoTable();
	}

	private void initRules() {
		int[][] initRule = {
				{ 0, 0 },
				{ Expr, 1 },
				{ Expr, 3 },
				{ Expr, 3 },
				{ Term, 1 },
				{ Term, 3 },
				{ Term, 3 },
				{ Factor, 1 },
				{ Factor, 3 },
				{ Factor, 4 },
				{ Args, 1 },
				{ Args, 0 },
				{ ArgumentList, 1 },
				{ ArgumentList, 3 },
		};

		this.rule = initRule;
	}

	private void initActionTable(){
		actionTable = new ActionElement[24][9];

		actionTable[0][NUM] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[0][LPAREN] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[0][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 14);
		actionTable[1][EOF] = new ActionElement(ActionElement.ACCEPT, 0);
		actionTable[1][PLUS] = new ActionElement(ActionElement.SHIFT, 2);
		actionTable[1][MINUS] = new ActionElement(ActionElement.SHIFT, 10);
		actionTable[2][NUM] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[2][LPAREN] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[2][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 14);
		actionTable[3][EOF] = new ActionElement(ActionElement.REDUCE, 2);
		actionTable[3][PLUS] = new ActionElement(ActionElement.REDUCE, 2);
		actionTable[3][MINUS] = new ActionElement(ActionElement.REDUCE, 2);
		actionTable[3][RPAREN] = new ActionElement(ActionElement.REDUCE, 2);
		actionTable[3][COMMA] = new ActionElement(ActionElement.REDUCE, 2);
		actionTable[3][PROD] = new ActionElement(ActionElement.SHIFT, 4);
		actionTable[3][DIV] = new ActionElement(ActionElement.SHIFT, 12);
		actionTable[4][NUM] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[4][LPAREN] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[4][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 14);
		actionTable[5][EOF] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[5][PLUS] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[5][MINUS] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[5][RPAREN] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[5][COMMA] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[5][PROD] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[5][DIV] = new ActionElement(ActionElement.REDUCE, 5);
		actionTable[6][EOF] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[6][PLUS] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[6][MINUS] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[6][RPAREN] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[6][COMMA] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[6][PROD] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[6][DIV] = new ActionElement(ActionElement.REDUCE, 7);
		actionTable[7][NUM] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[7][LPAREN] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[7][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 14);
		actionTable[8][RPAREN] = new ActionElement(ActionElement.SHIFT, 9);
		actionTable[8][PLUS] = new ActionElement(ActionElement.SHIFT, 2);
		actionTable[8][MINUS] = new ActionElement(ActionElement.SHIFT, 10);
		actionTable[9][EOF] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[9][PLUS] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[9][MINUS] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[9][RPAREN] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[9][COMMA] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[9][PROD] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[9][DIV] = new ActionElement(ActionElement.REDUCE, 8);
		actionTable[10][NUM] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[10][LPAREN] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[10][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 14);
		actionTable[11][EOF] = new ActionElement(ActionElement.REDUCE, 3);
		actionTable[11][PLUS] = new ActionElement(ActionElement.REDUCE, 3);
		actionTable[11][MINUS] = new ActionElement(ActionElement.REDUCE, 3);
		actionTable[11][RPAREN] = new ActionElement(ActionElement.REDUCE, 3);
		actionTable[11][COMMA] = new ActionElement(ActionElement.REDUCE, 3);
		actionTable[11][PROD] = new ActionElement(ActionElement.SHIFT, 4);
		actionTable[11][DIV] = new ActionElement(ActionElement.SHIFT, 12);
		actionTable[12][NUM] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[12][LPAREN] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[12][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 14);
		actionTable[13][EOF] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[13][PLUS] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[13][MINUS] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[13][RPAREN] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[13][COMMA] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[13][PROD] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[13][DIV] = new ActionElement(ActionElement.REDUCE, 6);
		actionTable[14][LPAREN] = new ActionElement(ActionElement.SHIFT, 15);
		actionTable[15][RPAREN] = new ActionElement(ActionElement.REDUCE, 11);
		actionTable[15][NUM] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[15][LPAREN] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[15][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 14);
		actionTable[16][RPAREN] = new ActionElement(ActionElement.SHIFT, 17);
		actionTable[17][EOF] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[17][PLUS] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[17][MINUS] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[17][RPAREN] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[17][COMMA] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[17][PROD] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[17][DIV] = new ActionElement(ActionElement.REDUCE, 9);
		actionTable[18][RPAREN] = new ActionElement(ActionElement.REDUCE, 10);
		actionTable[18][COMMA] = new ActionElement(ActionElement.SHIFT, 19);
		actionTable[19][NUM] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[19][LPAREN] = new ActionElement(ActionElement.SHIFT, 7);
		actionTable[19][IDENTIFIER] = new ActionElement(ActionElement.SHIFT, 14);
		actionTable[20][RPAREN] = new ActionElement(ActionElement.REDUCE, 13);
		actionTable[20][COMMA] = new ActionElement(ActionElement.REDUCE, 13);
		actionTable[20][PLUS] = new ActionElement(ActionElement.SHIFT, 2);
		actionTable[20][MINUS] = new ActionElement(ActionElement.SHIFT, 10);
		actionTable[21][EOF] = new ActionElement(ActionElement.REDUCE, 1);
		actionTable[21][PLUS] = new ActionElement(ActionElement.REDUCE, 1);
		actionTable[21][MINUS] = new ActionElement(ActionElement.REDUCE, 1);
		actionTable[21][RPAREN] = new ActionElement(ActionElement.REDUCE, 1);
		actionTable[21][COMMA] = new ActionElement(ActionElement.REDUCE, 1);
		actionTable[21][PROD] = new ActionElement(ActionElement.SHIFT, 4);
		actionTable[21][DIV] = new ActionElement(ActionElement.SHIFT, 12);
		actionTable[22][EOF] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[22][PLUS] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[22][MINUS] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[22][RPAREN] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[22][COMMA] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[22][PROD] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[22][DIV] = new ActionElement(ActionElement.REDUCE, 4);
		actionTable[23][RPAREN] = new ActionElement(ActionElement.REDUCE, 12);
		actionTable[23][COMMA] = new ActionElement(ActionElement.REDUCE, 12);
		actionTable[23][PLUS] = new ActionElement(ActionElement.SHIFT, 2);
		actionTable[23][MINUS] = new ActionElement(ActionElement.SHIFT, 10);	}

	private void initGotoTable() {
		gotoTable = new int[24][5];

		gotoTable[0][Expr] = 1;
		gotoTable[0][Term] = 21;
		gotoTable[0][Factor] = 22;
		gotoTable[2][Term] = 3;
		gotoTable[2][Factor] = 22;
		gotoTable[4][Factor] = 5;
		gotoTable[7][Expr] = 8;
		gotoTable[7][Term] = 21;
		gotoTable[7][Factor] = 22;
		gotoTable[10][Term] = 11;
		gotoTable[10][Factor] = 22;
		gotoTable[12][Factor] = 13;
		gotoTable[15][Args] = 16;
		gotoTable[15][ArgumentList] = 18;
		gotoTable[15][Expr] = 23;
		gotoTable[15][Term] = 21;
		gotoTable[15][Factor] = 22;
		gotoTable[19][Expr] = 20;
		gotoTable[19][Term] = 21;
		gotoTable[19][Factor] = 22;	}

}
