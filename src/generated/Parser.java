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
		gotoTable[0][0] = 0;
		gotoTable[1][1] = 2;
		gotoTable[2][2] = 3;
		gotoTable[3][3] = 0;
		gotoTable[4][4] = 0;
		gotoTable[5][5] = 0;
		gotoTable[6][6] = 0;
		gotoTable[7][7] = 8;
		gotoTable[8][8] = 0;
		gotoTable[9][9] = 0;
		gotoTable[10][10] = 0;
		gotoTable[11][11] = 12;
     }

}
