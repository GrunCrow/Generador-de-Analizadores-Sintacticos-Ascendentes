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
				{ Term, 1 },
				{ Term, 3 },
				{ Factor, 2 },
				{ Factor, 3 },
				{ Factor, 1 },
				{ Factor, 1 },
		};

		this.rule = initRule;
	}

	private void initActionTable(){
		actionTable = new ActionElement[15][7];

		actionTable[0][NOT] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[0][LPAREN] = new ActionElement(ActionElement.SHIFT, 8);
		actionTable[0][TRUE] = new ActionElement(ActionElement.SHIFT, 13);
		actionTable[0][FALSE] = new ActionElement(ActionElement.SHIFT, 14);

		actionTable[1][EOF] = new ActionElement(ActionElement.ACCEPT, 0);
		actionTable[1][OR] = new ActionElement(ActionElement.SHIFT, 2);

		actionTable[2][NOT] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[2][LPAREN] = new ActionElement(ActionElement.SHIFT, 8);
		actionTable[2][TRUE] = new ActionElement(ActionElement.SHIFT, 13);
		actionTable[2][FALSE] = new ActionElement(ActionElement.SHIFT, 14);

		actionTable[3][AND] = new ActionElement(ActionElement.SHIFT, 4);

		actionTable[4][NOT] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[4][LPAREN] = new ActionElement(ActionElement.SHIFT, 8);
		actionTable[4][TRUE] = new ActionElement(ActionElement.SHIFT, 13);
		actionTable[4][FALSE] = new ActionElement(ActionElement.SHIFT, 14);

		actionTable[6][NOT] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[6][LPAREN] = new ActionElement(ActionElement.SHIFT, 8);
		actionTable[6][TRUE] = new ActionElement(ActionElement.SHIFT, 13);
		actionTable[6][FALSE] = new ActionElement(ActionElement.SHIFT, 14);

		actionTable[8][NOT] = new ActionElement(ActionElement.SHIFT, 6);
		actionTable[8][LPAREN] = new ActionElement(ActionElement.SHIFT, 8);
		actionTable[8][TRUE] = new ActionElement(ActionElement.SHIFT, 13);
		actionTable[8][FALSE] = new ActionElement(ActionElement.SHIFT, 14);

		actionTable[9][RPAREN] = new ActionElement(ActionElement.SHIFT, 10);
		actionTable[9][OR] = new ActionElement(ActionElement.SHIFT, 2);

		actionTable[11][AND] = new ActionElement(ActionElement.SHIFT, 4);
	}

	private void initGotoTable() {
		gotoTable = new int[15][3];

		gotoTable[0][Expr] = 1;
		gotoTable[0][Term] = 11;
		gotoTable[0][Factor] = 12;

		gotoTable[2][Term] = 3;
		gotoTable[2][Factor] = 12;

		gotoTable[4][Factor] = 5;

		gotoTable[6][Factor] = 7;

		gotoTable[8][Expr] = 9;
		gotoTable[8][Term] = 11;
		gotoTable[8][Factor] = 12;
	}

}
