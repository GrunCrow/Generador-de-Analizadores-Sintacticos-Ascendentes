package generated;

import java.io.IOException;

import grammar_parser.*;

public class Parser extends SLRParser implements TokenConstants, SymbolConstants {

public Parser() throws SintaxException, IOException {
	initRules();
	initActionTable();
	initGotoTable();
}

    private void initRules() {
		int[][] initRule = {
			{ 0, 0 } ,
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
			{ ArgumentList, 3 }
		};
		this.rules = initRule;;
    }

    private void initActionTable() {
    }

	private void initGotoTable() {
		gotoTable = new int[13][5];
		gotoTable[0][Term] = 1;

		gotoTable[1][Expr] = 2;
		gotoTable[1][Term] = 2;

		gotoTable[2][Expr] = 3;
		gotoTable[2][Term] = 3;

		gotoTable[3][Factor] = 4;

		gotoTable[4][Term] = 5;
		gotoTable[4][Factor] = 5;

		gotoTable[5][Term] = 6;
		gotoTable[5][Factor] = 6;


		gotoTable[7][Expr] = 8;

		gotoTable[8][Args] = 9;

		gotoTable[9][ArgumentList] = 10;


		gotoTable[11][Expr] = 12;

		gotoTable[12][Expr] = 13;
		gotoTable[12][ArgumentList] = 13;

     }

}
