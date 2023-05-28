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
			{ 0, 1 },
			{ 0, 3 },
			{ 0, 3 },
			{ 1, 1 },
			{ 1, 3 },
			{ 1, 3 },
			{ 2, 1 },
			{ 2, 3 },
			{ 2, 4 },
			{ 3, 1 },
			{ 3, 0 },
			{ 4, 1 },
			{ 4, 3 }
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
