//------------------------------------------------------------------//
//                        COPYRIGHT NOTICE                          //
//------------------------------------------------------------------//
// Copyright (c) 2017, Francisco Jos� Moreno Velo                   //
// All rights reserved.                                             //
//                                                                  //
// Redistribution and use in source and binary forms, with or       //
// without modification, are permitted provided that the following  //
// conditions are met:                                              //
//                                                                  //
// * Redistributions of source code must retain the above copyright //
//   notice, this list of conditions and the following disclaimer.  // 
//                                                                  //
// * Redistributions in binary form must reproduce the above        // 
//   copyright notice, this list of conditions and the following    // 
//   disclaimer in the documentation and/or other materials         // 
//   provided with the distribution.                                //
//                                                                  //
// * Neither the name of the University of Huelva nor the names of  //
//   its contributors may be used to endorse or promote products    //
//   derived from this software without specific prior written      // 
//   permission.                                                    //
//                                                                  //
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND           // 
// CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,      // 
// INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF         // 
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE         // 
// DISCLAIMED. IN NO EVENT SHALL THE COPRIGHT OWNER OR CONTRIBUTORS //
// BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,         // 
// EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED  //
// TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,    //
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND   // 
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT          //
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING   //
// IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF   //
// THE POSSIBILITY OF SUCH DAMAGE.                                  //
//------------------------------------------------------------------//

//------------------------------------------------------------------//
//                      Universidad de Huelva                       //
//           Departamento de Tecnolog�as de la Informaci�n          //
//   �rea de Ciencias de la Computaci�n e Inteligencia Artificial   //
//------------------------------------------------------------------//
//                     PROCESADORES DE LENGUAJE                     //
//------------------------------------------------------------------//
//                                                                  //
//                  Compilador del lenguaje Tinto                   //
//                                                                  //
//------------------------------------------------------------------//

package grammar_parser;

import generated.*;

import java.io.*;

/**
 * Analizador sint�ctico ascendente del lenguaje Tinto
 * 
 * R0 ... X  ::= CompilationUnit
 * R1 ... CompilationUnit  ::=  ImportClauseList  TintoDecl
 * R2 ... ImportClauseList  ::=  ImportClauseList  ImportClause
 * R3 ... ImportClauseList  ::=  lambda
 * R4 ... ImportClause  ::=  import  identifier  semicolon
 * R5 ... TintoDecl ::= LibraryDecl
 * R6 ... TintoDecl ::= NativeDecl
 * R7 ... LibraryDecl  ::=  library  identifier  lbrace  FunctionList  rbrace
 * R8 ... FunctionList  ::=  FunctionList  FunctionDecl
 * R9 ... FunctionList  ::=  lambda
 * R10 ... FunctionDecl  ::=  Access  FunctionType   identifier   ArgumentDecl  FunctionBody
 * R11 ... NativeDecl  ::=  native  identifier  lbrace  NativeFunctionList  rbrace
 * R12 ... NativeFunctionList  ::=  NativeFunctionList  NativeFunctionDecl
 * R13 ... NativeFunctionList  ::=  lambda
 * R14 ... NativeFunctionDecl  ::=  Access  FunctionType   identifier   ArgumentDecl  semicolon
 * R15 ... Access ::= public
 * R16 .. Access ::= private
 * R17 .. FunctionType  ::=  Type
 * R18 .. FunctionType  ::=  void
 * R19 .. Type ::= int
 * R20 .. Type ::=  char
 * R21 .. Type ::=  boolean
 * R22 .. ArgumentDecl  ::=  lparen  rparen
 * R23 .. ArgumentDecl  ::=  lparen  ArgumentList  rparen
 * R24 .. ArgumentList  ::=  Argument
 * R25 .. ArgumentList  ::=  ArgumentList  comma  Argument
 * R26 .. Argument  ::=  Type  identifier
 * R27 .. FunctionBody  ::= lbrace StatementList rbrace
 * R28 .. StatementList  ::=  StatementList  Statement
 * R29 .. StatementList  ::=  lambda
 * R30 .. Statement  ::=  Decl
 * R31 .. Statement  ::=  IdStm
 * R32 .. Statement  ::=  IfStm
 * R33 .. Statement  ::=  WhileStm
 * R34 .. Statement  ::=  ReturnStm
 * R35 .. Statement  ::=  NoStm
 * R36 .. Statement  ::=  BlockStm
 * R37 .. Decl  ::=  Type  IdList  semicolon
 * R38 .. IdList  ::=   identifier
 * R39 .. IdList  ::=   identifier   assign   Expr
 * R40 .. IdList  ::=  IdList   comma  identifier
 * R41 .. IdList  ::=  IdList   comma  identifier   assign   Expr
 * R42 .. IfStm  ::=  if   lparen   Expr  rparen   Statement
 * R43 .. IfStm  ::=  if   lparen   Expr  rparen   Statement  else  Statement
 * R44 .. WhileStm  ::=  while   lparen  Expr   rparen   Statement
 * R45 .. ReturnStm  ::=  return  Expr  semicolon
 * R46 .. ReturnStm  ::=  return  semicolon
 * R47 .. NoStm  ::=  semicolon
 * R48 .. IdStm  ::=  identifier   assign   Expr  semicolon
 * R49 .. IdStm  ::=  identifier  FunctionCall   semicolon
 * R50 .. IdStm  ::=  identifier  dot   identifier  FunctionCall  semicolon
 * R51 .. BlockStm  ::=  lbrace  StatementList  rbrace
 * R52 .. Expr  ::=  AndExpr
 * R53 .. Expr  ::=  Expr   or  AndExpr
 * R54 .. AndExpr  ::=  RelExpr
 * R55 .. AndExpr  ::=  AndExpr  and  RelExpr
 * R56 .. RelExpr  ::=  SumExpr
 * R57 .. RelExpr  ::=   SumExpr  eq  SumExpr
 * R58 .. RelExpr  ::=   SumExpr  ne  SumExpr
 * R59 .. RelExpr  ::=   SumExpr  gt  SumExpr
 * R60 .. RelExpr  ::=   SumExpr  ge  SumExpr
 * R61 .. RelExpr  ::=   SumExpr  lt  SumExpr
 * R62 .. RelExpr  ::=   SumExpr  le  SumExpr
 * R63 .. SumExpr  ::=  not  ProdExpr
 * R64 .. SumExpr  ::=  minus  ProdExpr
 * R65 .. SumExpr  ::=  plus  ProdExpr
 * R66 .. SumExpr  ::=  ProdExpr
 * R67 .. SumExpr  ::=  SumExpr  minus  ProdExpr
 * R68 .. SumExpr  ::=  SumExpr  plus  ProdExpr
 * R69 .. ProdExpr  ::=  Factor
 * R70 .. ProdExpr  ::=  ProdExpr  prod  Factor
 * R71 .. ProdExpr  ::=  ProdExpr  div   Factor
 * R72 .. ProdExpr  ::=  ProdExpr  mod  Factor
 * R73 .. Factor  ::=  Literal
 * R74 .. Factor  ::=  Reference
 * R75 .. Factor  ::=  lparen   Expr   rparen
 * R76 .. Literal  ::=  integer_literal
 * R77 .. Literal  ::=  char_literal
 * R78 .. Literal  ::=  true
 * R79 .. Literal  ::=  false
 * R80 .. Reference  ::=  identifier
 * R81 .. Reference  ::=  identifier  FunctionCall
 * R82 .. Reference  ::=  identifier  dot   identifier  FunctionCall
 * R83 .. FunctionCall  ::=  lparen  rparen
 * R84 .. FunctionCall  ::=  lparen  ExprList  rparen
 * R85 .. ExprList  ::=  Expr
 * R86 .. ExprList  ::=  ExprList  comma  Expr
 * 
 * @author Francisco José Moreno Velo
 */
public class TintoParser extends SLRParser implements TokenConstants, SymbolConstants {

	/**
	 * Constructor
	 *
	 */
	public TintoParser() 
	{
		initRules();
		initActionTable();
		initGotoTable();
	}
	
	/**
	 * M�todo de an�lisis de un fichero
	 * @param filename
	 * @return
	 * @throws IOException 
	 */
	public boolean parse(File file) throws IOException, SintaxException
	{
		return parse(new TintoLexer(file));
	}
	

	/**
	 * Crea la matriz de reglas
	 *
	 */
	private void initRules() {
		int[][] initRule = {
				{ 0, 0 } ,
				{ S_COMPILATION_UNIT, 2 },
				{ S_IMPORT_CLAUSE_LIST, 2 },
				{ S_IMPORT_CLAUSE_LIST, 0 },
				{ S_IMPORT_CLAUSE, 3 },
				{ S_TINTO_DECL, 1},
				{ S_TINTO_DECL, 1},
				{ S_LIBRARY_DECL, 5 },
				{ S_FUNCTION_LIST, 2 },
				{ S_FUNCTION_LIST, 0 },
				{ S_FUNCTION_DECL, 5 },
				{ S_NATIVE_DECL, 5 },
				{ S_NATIVE_FUNCTION_LIST, 2 },
				{ S_NATIVE_FUNCTION_LIST, 0 },
				{ S_NATIVE_FUNCTION_DECL, 5 },
				{ S_ACCESS, 1},
				{ S_ACCESS, 1},
				{ S_FUNCTION_TYPE, 1 },
				{ S_FUNCTION_TYPE, 1 },
				{ S_TYPE, 1 },
				{ S_TYPE, 1 },
				{ S_TYPE, 1 },
				{ S_ARGUMENT_DECL, 2 },
				{ S_ARGUMENT_DECL, 3 },
				{ S_ARGUMENT_LIST, 1 },
				{ S_ARGUMENT_LIST, 3 },
				{ S_ARGUMENT, 2 },
				{ S_FUNCTION_BODY, 3 },
				{ S_STATEMENT_LIST, 2 },
				{ S_STATEMENT_LIST, 0 },
				{ S_STATEMENT, 1 },
				{ S_STATEMENT, 1 },
				{ S_STATEMENT, 1 },
				{ S_STATEMENT, 1 },
				{ S_STATEMENT, 1 },
				{ S_STATEMENT, 1 },
				{ S_STATEMENT, 1 },
				{ S_DECL, 3 },
				{ S_ID_LIST, 1 },
				{ S_ID_LIST, 3 },
				{ S_ID_LIST, 3 },
				{ S_ID_LIST, 5 },
				{ S_IF_STM, 5 },
				{ S_IF_STM, 7 },
				{ S_WHILE_STM, 5 },
				{ S_RETURN_STM, 3 },
				{ S_RETURN_STM, 2 },
				{ S_NO_STM, 1 },
				{ S_ID_STM, 4 },
				{ S_ID_STM, 3 },
				{ S_ID_STM, 5 },
				{ S_BLOCK_STM, 3 },
				{ S_EXPR, 1 },
				{ S_EXPR, 3 },
				{ S_AND_EXPR, 1 },
				{ S_AND_EXPR, 3 },
				{ S_REL_EXPR, 1 },
				{ S_REL_EXPR, 3 },
				{ S_REL_EXPR, 3 },
				{ S_REL_EXPR, 3 },
				{ S_REL_EXPR, 3 },
				{ S_REL_EXPR, 3 },
				{ S_REL_EXPR, 3 },
				{ S_SUM_EXPR, 2 },
				{ S_SUM_EXPR, 2 },
				{ S_SUM_EXPR, 2 },
				{ S_SUM_EXPR, 1 },
				{ S_SUM_EXPR, 3 },
				{ S_SUM_EXPR, 3 },
				{ S_PROD_EXPR, 1 },
				{ S_PROD_EXPR, 3 },
				{ S_PROD_EXPR, 3 },
				{ S_PROD_EXPR, 3 },
				{ S_FACTOR, 1 },
				{ S_FACTOR, 1 },
				{ S_FACTOR, 3 },
				{ S_LITERAL, 1 },
				{ S_LITERAL, 1 },
				{ S_LITERAL, 1 },
				{ S_LITERAL, 1 },
				{ S_REFERENCE, 1 },
				{ S_REFERENCE, 2 },
				{ S_REFERENCE, 4 },
				{ S_FUNCTION_CALL, 2 },
				{ S_FUNCTION_CALL, 3 },
				{ S_EXPR_LIST, 1 },
				{ S_EXPR_LIST, 3 }
		};

		this.rule = initRule;
	}
	
	/**
	 * Inicializa la tabla de acciones
	 *
	 */
	private void initActionTable() {
		actionTable = new ActionElement[157][41]; // 157 estados, 41 tokens
		
		actionTable[0][IMPORT] = new ActionElement(ActionElement.REDUCE,3);
		actionTable[0][LIBRARY] = new ActionElement(ActionElement.REDUCE,3);
		actionTable[0][NATIVE] = new ActionElement(ActionElement.REDUCE,3);

		actionTable[1][EOF] = new ActionElement(ActionElement.ACCEPT,0);

		actionTable[2][IMPORT] = new ActionElement(ActionElement.SHIFT,5);
		actionTable[2][LIBRARY] = new ActionElement(ActionElement.SHIFT,8);
		actionTable[2][NATIVE] = new ActionElement(ActionElement.SHIFT,9);

		actionTable[3][EOF] = new ActionElement(ActionElement.REDUCE,1);

		actionTable[4][IMPORT] = new ActionElement(ActionElement.REDUCE,2);
		actionTable[4][LIBRARY] = new ActionElement(ActionElement.REDUCE,2);
		actionTable[4][NATIVE] = new ActionElement(ActionElement.REDUCE,2);

		actionTable[5][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,10);

		actionTable[6][EOF] = new ActionElement(ActionElement.REDUCE,5);

		actionTable[7][EOF] = new ActionElement(ActionElement.REDUCE,6);

		actionTable[8][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,11);

		actionTable[9][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,12);

		actionTable[10][SEMICOLON] = new ActionElement(ActionElement.SHIFT,13);

		actionTable[11][LBRACE] = new ActionElement(ActionElement.SHIFT,14);

		actionTable[12][LBRACE] = new ActionElement(ActionElement.SHIFT,15);

		actionTable[13][IMPORT] = new ActionElement(ActionElement.REDUCE,4);
		actionTable[13][LIBRARY] = new ActionElement(ActionElement.REDUCE,4);
		actionTable[13][NATIVE] = new ActionElement(ActionElement.REDUCE,4);

		actionTable[14][RBRACE] = new ActionElement(ActionElement.REDUCE,9);
		actionTable[14][PUBLIC] = new ActionElement(ActionElement.REDUCE,9);
		actionTable[14][PRIVATE] = new ActionElement(ActionElement.REDUCE,9);

		actionTable[15][RBRACE] = new ActionElement(ActionElement.REDUCE,13);
		actionTable[15][PUBLIC] = new ActionElement(ActionElement.REDUCE,13);
		actionTable[15][PRIVATE] = new ActionElement(ActionElement.REDUCE,13);
		
		actionTable[16][RBRACE] = new ActionElement(ActionElement.SHIFT,18);
		actionTable[16][PUBLIC] = new ActionElement(ActionElement.SHIFT,21);
		actionTable[16][PRIVATE] = new ActionElement(ActionElement.SHIFT,22);

		actionTable[17][RBRACE] = new ActionElement(ActionElement.SHIFT,23);
		actionTable[17][PUBLIC] = new ActionElement(ActionElement.SHIFT,21);
		actionTable[17][PRIVATE] = new ActionElement(ActionElement.SHIFT,22);
		
		actionTable[18][EOF] = new ActionElement(ActionElement.REDUCE,7);

		actionTable[19][RBRACE] = new ActionElement(ActionElement.REDUCE,8);
		actionTable[19][PUBLIC] = new ActionElement(ActionElement.REDUCE,8);
		actionTable[19][PRIVATE] = new ActionElement(ActionElement.REDUCE,8);

		actionTable[20][VOID] = new ActionElement(ActionElement.SHIFT,28);
		actionTable[20][INT] = new ActionElement(ActionElement.SHIFT,29);
		actionTable[20][CHAR] = new ActionElement(ActionElement.SHIFT,30);
		actionTable[20][BOOLEAN] = new ActionElement(ActionElement.SHIFT,31);

		actionTable[21][VOID] = new ActionElement(ActionElement.REDUCE,15);
		actionTable[21][INT] = new ActionElement(ActionElement.REDUCE,15);
		actionTable[21][CHAR] = new ActionElement(ActionElement.REDUCE,15);
		actionTable[21][BOOLEAN] = new ActionElement(ActionElement.REDUCE,15);

		actionTable[22][VOID] = new ActionElement(ActionElement.REDUCE,16);
		actionTable[22][INT] = new ActionElement(ActionElement.REDUCE,16);
		actionTable[22][CHAR] = new ActionElement(ActionElement.REDUCE,16);
		actionTable[22][BOOLEAN] = new ActionElement(ActionElement.REDUCE,16);

		actionTable[23][EOF] = new ActionElement(ActionElement.REDUCE,11);

		actionTable[24][RBRACE] = new ActionElement(ActionElement.REDUCE,12);
		actionTable[24][PUBLIC] = new ActionElement(ActionElement.REDUCE,12);
		actionTable[24][PRIVATE] = new ActionElement(ActionElement.REDUCE,12);
		
		actionTable[25][VOID] = new ActionElement(ActionElement.SHIFT,28);
		actionTable[25][INT] = new ActionElement(ActionElement.SHIFT,29);
		actionTable[25][CHAR] = new ActionElement(ActionElement.SHIFT,30);
		actionTable[25][BOOLEAN] = new ActionElement(ActionElement.SHIFT,31);
		
		actionTable[26][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,33);

		actionTable[27][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,17);

		actionTable[28][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,18);

		actionTable[29][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,19);

		actionTable[30][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,20);

		actionTable[31][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,21);

		actionTable[32][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,34);

		actionTable[33][LPAREN] = new ActionElement(ActionElement.SHIFT,36);

		actionTable[34][LPAREN] = new ActionElement(ActionElement.SHIFT,36);

		actionTable[35][LBRACE] = new ActionElement(ActionElement.SHIFT,39);

		actionTable[36][RPAREN] = new ActionElement(ActionElement.SHIFT,40);
		actionTable[36][INT] = new ActionElement(ActionElement.SHIFT,29);
		actionTable[36][CHAR] = new ActionElement(ActionElement.SHIFT,30);
		actionTable[36][BOOLEAN] = new ActionElement(ActionElement.SHIFT,31);

		actionTable[37][SEMICOLON] = new ActionElement(ActionElement.SHIFT,44);
		
		actionTable[38][RBRACE] = new ActionElement(ActionElement.REDUCE,10);
		actionTable[38][PUBLIC] = new ActionElement(ActionElement.REDUCE,10);
		actionTable[38][PRIVATE] = new ActionElement(ActionElement.REDUCE,10);

		actionTable[39][RBRACE] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[39][INT] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[39][CHAR] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[39][BOOLEAN] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[39][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[39][IF] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[39][WHILE] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[39][RETURN] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[39][SEMICOLON] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[39][LBRACE] = new ActionElement(ActionElement.REDUCE,29);

		actionTable[40][LBRACE] = new ActionElement(ActionElement.REDUCE,22);
		actionTable[40][SEMICOLON] = new ActionElement(ActionElement.REDUCE,22);

		actionTable[41][RPAREN] = new ActionElement(ActionElement.SHIFT,46);
		actionTable[41][COMMA] = new ActionElement(ActionElement.SHIFT,47);
		
		actionTable[42][RPAREN] = new ActionElement(ActionElement.REDUCE,24);
		actionTable[42][COMMA] = new ActionElement(ActionElement.REDUCE,24);

		actionTable[43][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,48);
		
		actionTable[44][RBRACE] = new ActionElement(ActionElement.REDUCE,14);
		actionTable[44][PUBLIC] = new ActionElement(ActionElement.REDUCE,14);
		actionTable[44][PRIVATE] = new ActionElement(ActionElement.REDUCE,14);		
		
		actionTable[45][RBRACE] = new ActionElement(ActionElement.SHIFT,49);
		actionTable[45][INT] = new ActionElement(ActionElement.SHIFT,29);
		actionTable[45][CHAR] = new ActionElement(ActionElement.SHIFT,30);
		actionTable[45][BOOLEAN] = new ActionElement(ActionElement.SHIFT,31);
		actionTable[45][IF] = new ActionElement(ActionElement.SHIFT,59);
		actionTable[45][WHILE] = new ActionElement(ActionElement.SHIFT,60);
		actionTable[45][RETURN] = new ActionElement(ActionElement.SHIFT,61);
		actionTable[45][SEMICOLON] = new ActionElement(ActionElement.SHIFT,62);
		actionTable[45][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,63);
		actionTable[45][LBRACE] = new ActionElement(ActionElement.SHIFT,64);

		actionTable[46][LBRACE] = new ActionElement(ActionElement.REDUCE,23);
		actionTable[46][SEMICOLON] = new ActionElement(ActionElement.REDUCE,23);

		actionTable[47][INT] = new ActionElement(ActionElement.SHIFT,29);
		actionTable[47][CHAR] = new ActionElement(ActionElement.SHIFT,30);
		actionTable[47][BOOLEAN] = new ActionElement(ActionElement.SHIFT,31);

		actionTable[48][RPAREN] = new ActionElement(ActionElement.REDUCE,26);
		actionTable[48][COMMA] = new ActionElement(ActionElement.REDUCE,26);

		actionTable[49][RBRACE] = new ActionElement(ActionElement.REDUCE,27);
		actionTable[49][PUBLIC] = new ActionElement(ActionElement.REDUCE,27);
		actionTable[49][PRIVATE] = new ActionElement(ActionElement.REDUCE,27);

		actionTable[50][RBRACE] = new ActionElement(ActionElement.REDUCE,28);
		actionTable[50][INT] = new ActionElement(ActionElement.REDUCE,28);
		actionTable[50][CHAR] = new ActionElement(ActionElement.REDUCE,28);
		actionTable[50][BOOLEAN] = new ActionElement(ActionElement.REDUCE,28);
		actionTable[50][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,28);
		actionTable[50][IF] = new ActionElement(ActionElement.REDUCE,28);
		actionTable[50][WHILE] = new ActionElement(ActionElement.REDUCE,28);
		actionTable[50][RETURN] = new ActionElement(ActionElement.REDUCE,28);
		actionTable[50][SEMICOLON] = new ActionElement(ActionElement.REDUCE,28);
		actionTable[50][LBRACE] = new ActionElement(ActionElement.REDUCE,28);

		actionTable[51][RBRACE] = new ActionElement(ActionElement.REDUCE,30);
		actionTable[51][INT] = new ActionElement(ActionElement.REDUCE,30);
		actionTable[51][CHAR] = new ActionElement(ActionElement.REDUCE,30);
		actionTable[51][BOOLEAN] = new ActionElement(ActionElement.REDUCE,30);
		actionTable[51][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,30);
		actionTable[51][IF] = new ActionElement(ActionElement.REDUCE,30);
		actionTable[51][WHILE] = new ActionElement(ActionElement.REDUCE,30);
		actionTable[51][RETURN] = new ActionElement(ActionElement.REDUCE,30);
		actionTable[51][SEMICOLON] = new ActionElement(ActionElement.REDUCE,30);
		actionTable[51][LBRACE] = new ActionElement(ActionElement.REDUCE,30);
		actionTable[51][ELSE] = new ActionElement(ActionElement.REDUCE,30);

		actionTable[52][RBRACE] = new ActionElement(ActionElement.REDUCE,31);
		actionTable[52][INT] = new ActionElement(ActionElement.REDUCE,31);
		actionTable[52][CHAR] = new ActionElement(ActionElement.REDUCE,31);
		actionTable[52][BOOLEAN] = new ActionElement(ActionElement.REDUCE,31);
		actionTable[52][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,31);
		actionTable[52][IF] = new ActionElement(ActionElement.REDUCE,31);
		actionTable[52][WHILE] = new ActionElement(ActionElement.REDUCE,31);
		actionTable[52][RETURN] = new ActionElement(ActionElement.REDUCE,31);
		actionTable[52][SEMICOLON] = new ActionElement(ActionElement.REDUCE,31);
		actionTable[52][LBRACE] = new ActionElement(ActionElement.REDUCE,31);
		actionTable[52][ELSE] = new ActionElement(ActionElement.REDUCE,31);

		actionTable[53][RBRACE] = new ActionElement(ActionElement.REDUCE,32);
		actionTable[53][INT] = new ActionElement(ActionElement.REDUCE,32);
		actionTable[53][CHAR] = new ActionElement(ActionElement.REDUCE,32);
		actionTable[53][BOOLEAN] = new ActionElement(ActionElement.REDUCE,32);
		actionTable[53][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,32);
		actionTable[53][IF] = new ActionElement(ActionElement.REDUCE,32);
		actionTable[53][WHILE] = new ActionElement(ActionElement.REDUCE,32);
		actionTable[53][RETURN] = new ActionElement(ActionElement.REDUCE,32);
		actionTable[53][SEMICOLON] = new ActionElement(ActionElement.REDUCE,32);
		actionTable[53][LBRACE] = new ActionElement(ActionElement.REDUCE,32);
		actionTable[53][ELSE] = new ActionElement(ActionElement.REDUCE,32);

		actionTable[54][RBRACE] = new ActionElement(ActionElement.REDUCE,33);
		actionTable[54][INT] = new ActionElement(ActionElement.REDUCE,33);
		actionTable[54][CHAR] = new ActionElement(ActionElement.REDUCE,33);
		actionTable[54][BOOLEAN] = new ActionElement(ActionElement.REDUCE,33);
		actionTable[54][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,33);
		actionTable[54][IF] = new ActionElement(ActionElement.REDUCE,33);
		actionTable[54][WHILE] = new ActionElement(ActionElement.REDUCE,33);
		actionTable[54][RETURN] = new ActionElement(ActionElement.REDUCE,33);
		actionTable[54][SEMICOLON] = new ActionElement(ActionElement.REDUCE,33);
		actionTable[54][LBRACE] = new ActionElement(ActionElement.REDUCE,33);
		actionTable[54][ELSE] = new ActionElement(ActionElement.REDUCE,33);

		actionTable[55][RBRACE] = new ActionElement(ActionElement.REDUCE,34);
		actionTable[55][INT] = new ActionElement(ActionElement.REDUCE,34);
		actionTable[55][CHAR] = new ActionElement(ActionElement.REDUCE,34);
		actionTable[55][BOOLEAN] = new ActionElement(ActionElement.REDUCE,34);
		actionTable[55][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,34);
		actionTable[55][IF] = new ActionElement(ActionElement.REDUCE,34);
		actionTable[55][WHILE] = new ActionElement(ActionElement.REDUCE,34);
		actionTable[55][RETURN] = new ActionElement(ActionElement.REDUCE,34);
		actionTable[55][SEMICOLON] = new ActionElement(ActionElement.REDUCE,34);
		actionTable[55][LBRACE] = new ActionElement(ActionElement.REDUCE,34);
		actionTable[55][ELSE] = new ActionElement(ActionElement.REDUCE,34);

		actionTable[56][RBRACE] = new ActionElement(ActionElement.REDUCE,35);
		actionTable[56][INT] = new ActionElement(ActionElement.REDUCE,35);
		actionTable[56][CHAR] = new ActionElement(ActionElement.REDUCE,35);
		actionTable[56][BOOLEAN] = new ActionElement(ActionElement.REDUCE,35);
		actionTable[56][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,35);
		actionTable[56][IF] = new ActionElement(ActionElement.REDUCE,35);
		actionTable[56][WHILE] = new ActionElement(ActionElement.REDUCE,35);
		actionTable[56][RETURN] = new ActionElement(ActionElement.REDUCE,35);
		actionTable[56][SEMICOLON] = new ActionElement(ActionElement.REDUCE,35);
		actionTable[56][LBRACE] = new ActionElement(ActionElement.REDUCE,35);
		actionTable[56][ELSE] = new ActionElement(ActionElement.REDUCE,35);

		actionTable[57][RBRACE] = new ActionElement(ActionElement.REDUCE,36);
		actionTable[57][INT] = new ActionElement(ActionElement.REDUCE,36);
		actionTable[57][CHAR] = new ActionElement(ActionElement.REDUCE,36);
		actionTable[57][BOOLEAN] = new ActionElement(ActionElement.REDUCE,36);
		actionTable[57][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,36);
		actionTable[57][IF] = new ActionElement(ActionElement.REDUCE,36);
		actionTable[57][WHILE] = new ActionElement(ActionElement.REDUCE,36);
		actionTable[57][RETURN] = new ActionElement(ActionElement.REDUCE,36);
		actionTable[57][SEMICOLON] = new ActionElement(ActionElement.REDUCE,36);
		actionTable[57][LBRACE] = new ActionElement(ActionElement.REDUCE,36);
		actionTable[57][ELSE] = new ActionElement(ActionElement.REDUCE,36);

		actionTable[58][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,67);
		
		actionTable[59][LPAREN] = new ActionElement(ActionElement.SHIFT,68);

		actionTable[60][LPAREN] = new ActionElement(ActionElement.SHIFT,69);

		actionTable[61][SEMICOLON] = new ActionElement(ActionElement.SHIFT,71);
		actionTable[61][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[61][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[61][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[61][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[61][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[61][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[61][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[61][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[61][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[62][RBRACE] = new ActionElement(ActionElement.REDUCE,47);
		actionTable[62][INT] = new ActionElement(ActionElement.REDUCE,47);
		actionTable[62][CHAR] = new ActionElement(ActionElement.REDUCE,47);
		actionTable[62][BOOLEAN] = new ActionElement(ActionElement.REDUCE,47);
		actionTable[62][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,47);
		actionTable[62][IF] = new ActionElement(ActionElement.REDUCE,47);
		actionTable[62][WHILE] = new ActionElement(ActionElement.REDUCE,47);
		actionTable[62][RETURN] = new ActionElement(ActionElement.REDUCE,47);
		actionTable[62][SEMICOLON] = new ActionElement(ActionElement.REDUCE,47);
		actionTable[62][LBRACE] = new ActionElement(ActionElement.REDUCE,47);
		actionTable[62][ELSE] = new ActionElement(ActionElement.REDUCE,47);

		actionTable[63][ASSIGN] = new ActionElement(ActionElement.SHIFT,88);
		actionTable[63][DOT] = new ActionElement(ActionElement.SHIFT,90);
		actionTable[63][LPAREN] = new ActionElement(ActionElement.SHIFT,91);

		actionTable[64][RBRACE] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[64][INT] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[64][CHAR] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[64][BOOLEAN] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[64][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[64][IF] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[64][WHILE] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[64][RETURN] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[64][SEMICOLON] = new ActionElement(ActionElement.REDUCE,29);
		actionTable[64][LBRACE] = new ActionElement(ActionElement.REDUCE,29);

		actionTable[65][RPAREN] = new ActionElement(ActionElement.REDUCE,25);
		actionTable[65][COMMA] = new ActionElement(ActionElement.REDUCE,25);

		actionTable[66][SEMICOLON] = new ActionElement(ActionElement.SHIFT,93);
		actionTable[66][COMMA] = new ActionElement(ActionElement.SHIFT,94);

		actionTable[67][ASSIGN] = new ActionElement(ActionElement.SHIFT,95);
		actionTable[67][SEMICOLON] = new ActionElement(ActionElement.REDUCE,38);
		actionTable[67][COMMA] = new ActionElement(ActionElement.REDUCE,38);

		actionTable[68][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[68][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[68][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[68][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[68][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[68][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[68][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[68][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[68][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[69][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[69][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[69][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[69][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[69][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[69][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[69][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[69][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[69][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[70][SEMICOLON] = new ActionElement(ActionElement.SHIFT,98);
		actionTable[70][OR] = new ActionElement(ActionElement.SHIFT,99);

		actionTable[71][RBRACE] = new ActionElement(ActionElement.REDUCE,46);
		actionTable[71][INT] = new ActionElement(ActionElement.REDUCE,46);
		actionTable[71][CHAR] = new ActionElement(ActionElement.REDUCE,46);
		actionTable[71][BOOLEAN] = new ActionElement(ActionElement.REDUCE,46);
		actionTable[71][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,46);
		actionTable[71][IF] = new ActionElement(ActionElement.REDUCE,46);
		actionTable[71][WHILE] = new ActionElement(ActionElement.REDUCE,46);
		actionTable[71][RETURN] = new ActionElement(ActionElement.REDUCE,46);
		actionTable[71][SEMICOLON] = new ActionElement(ActionElement.REDUCE,46);
		actionTable[71][LBRACE] = new ActionElement(ActionElement.REDUCE,46);
		actionTable[71][ELSE] = new ActionElement(ActionElement.REDUCE,46);
		
		actionTable[72][AND] = new ActionElement(ActionElement.SHIFT,100);
		actionTable[72][COMMA] = new ActionElement(ActionElement.REDUCE,52);
		actionTable[72][SEMICOLON] = new ActionElement(ActionElement.REDUCE,52);
		actionTable[72][RPAREN] = new ActionElement(ActionElement.REDUCE,52);
		actionTable[72][OR] = new ActionElement(ActionElement.REDUCE,52);
	
		actionTable[73][COMMA] = new ActionElement(ActionElement.REDUCE,54);
		actionTable[73][SEMICOLON] = new ActionElement(ActionElement.REDUCE,54);
		actionTable[73][RPAREN] = new ActionElement(ActionElement.REDUCE,54);
		actionTable[73][OR] = new ActionElement(ActionElement.REDUCE,54);
		actionTable[73][AND] = new ActionElement(ActionElement.REDUCE,54);

		actionTable[74][COMMA] = new ActionElement(ActionElement.REDUCE,56);
		actionTable[74][SEMICOLON] = new ActionElement(ActionElement.REDUCE,56);
		actionTable[74][RPAREN] = new ActionElement(ActionElement.REDUCE,56);
		actionTable[74][OR] = new ActionElement(ActionElement.REDUCE,56);
		actionTable[74][AND] = new ActionElement(ActionElement.REDUCE,56);
		actionTable[74][EQ] = new ActionElement(ActionElement.SHIFT,101);
		actionTable[74][NE] = new ActionElement(ActionElement.SHIFT,102);
		actionTable[74][GT] = new ActionElement(ActionElement.SHIFT,103);
		actionTable[74][GE] = new ActionElement(ActionElement.SHIFT,104);
		actionTable[74][LT] = new ActionElement(ActionElement.SHIFT,105);
		actionTable[74][LE] = new ActionElement(ActionElement.SHIFT,106);
		actionTable[74][MINUS] = new ActionElement(ActionElement.SHIFT,107);
		actionTable[74][PLUS] = new ActionElement(ActionElement.SHIFT,108);

		actionTable[75][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[75][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[75][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[75][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[75][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[75][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[76][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[76][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[76][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[76][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[76][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[76][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);
	
		actionTable[77][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[77][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[77][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[77][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[77][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[77][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[78][COMMA] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][SEMICOLON] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][RPAREN] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][OR] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][AND] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][EQ] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][NE] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][GT] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][GE] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][LT] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][LE] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][MINUS] = new ActionElement(ActionElement.REDUCE,66);
		actionTable[78][PLUS] = new ActionElement(ActionElement.REDUCE,66);	
		actionTable[78][PROD] = new ActionElement(ActionElement.SHIFT,112);
		actionTable[78][DIV] = new ActionElement(ActionElement.SHIFT,113);
		actionTable[78][MOD] = new ActionElement(ActionElement.SHIFT,114);

		actionTable[79][COMMA] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][SEMICOLON] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][RPAREN] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][OR] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][AND] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][EQ] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][NE] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][GT] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][GE] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][LT] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][LE] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][MINUS] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][PLUS] = new ActionElement(ActionElement.REDUCE,69);	
		actionTable[79][PROD] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][DIV] = new ActionElement(ActionElement.REDUCE,69);
		actionTable[79][MOD] = new ActionElement(ActionElement.REDUCE,69);	
		
		actionTable[80][COMMA] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][SEMICOLON] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][RPAREN] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][OR] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][AND] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][EQ] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][NE] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][GT] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][GE] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][LT] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][LE] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][MINUS] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][PLUS] = new ActionElement(ActionElement.REDUCE,73);	
		actionTable[80][PROD] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][DIV] = new ActionElement(ActionElement.REDUCE,73);
		actionTable[80][MOD] = new ActionElement(ActionElement.REDUCE,73);	
		
		actionTable[81][COMMA] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][SEMICOLON] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][RPAREN] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][OR] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][AND] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][EQ] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][NE] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][GT] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][GE] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][LT] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][LE] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][MINUS] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][PLUS] = new ActionElement(ActionElement.REDUCE,74);	
		actionTable[81][PROD] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][DIV] = new ActionElement(ActionElement.REDUCE,74);
		actionTable[81][MOD] = new ActionElement(ActionElement.REDUCE,74);	

		actionTable[82][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[82][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[82][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[82][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[82][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[82][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[82][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[82][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[82][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);
		
		actionTable[83][COMMA] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][SEMICOLON] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][RPAREN] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][OR] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][AND] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][EQ] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][NE] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][GT] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][GE] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][LT] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][LE] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][MINUS] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][PLUS] = new ActionElement(ActionElement.REDUCE,76);	
		actionTable[83][PROD] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][DIV] = new ActionElement(ActionElement.REDUCE,76);
		actionTable[83][MOD] = new ActionElement(ActionElement.REDUCE,76);	

		actionTable[84][COMMA] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][SEMICOLON] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][RPAREN] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][OR] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][AND] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][EQ] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][NE] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][GT] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][GE] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][LT] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][LE] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][MINUS] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][PLUS] = new ActionElement(ActionElement.REDUCE,77);	
		actionTable[84][PROD] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][DIV] = new ActionElement(ActionElement.REDUCE,77);
		actionTable[84][MOD] = new ActionElement(ActionElement.REDUCE,77);			
		
		actionTable[85][COMMA] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][SEMICOLON] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][RPAREN] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][OR] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][AND] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][EQ] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][NE] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][GT] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][GE] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][LT] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][LE] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][MINUS] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][PLUS] = new ActionElement(ActionElement.REDUCE,78);	
		actionTable[85][PROD] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][DIV] = new ActionElement(ActionElement.REDUCE,78);
		actionTable[85][MOD] = new ActionElement(ActionElement.REDUCE,78);
		
		actionTable[86][COMMA] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][SEMICOLON] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][RPAREN] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][OR] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][AND] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][EQ] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][NE] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][GT] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][GE] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][LT] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][LE] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][MINUS] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][PLUS] = new ActionElement(ActionElement.REDUCE,79);	
		actionTable[86][PROD] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][DIV] = new ActionElement(ActionElement.REDUCE,79);
		actionTable[86][MOD] = new ActionElement(ActionElement.REDUCE,79);
		
		actionTable[87][COMMA] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][SEMICOLON] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][RPAREN] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][OR] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][AND] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][EQ] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][NE] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][GT] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][GE] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][LT] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][LE] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][MINUS] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][PLUS] = new ActionElement(ActionElement.REDUCE,80);	
		actionTable[87][PROD] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][DIV] = new ActionElement(ActionElement.REDUCE,80);
		actionTable[87][MOD] = new ActionElement(ActionElement.REDUCE,80);	
		actionTable[87][DOT] = new ActionElement(ActionElement.SHIFT,117);	
		actionTable[87][LPAREN] = new ActionElement(ActionElement.SHIFT,91);	
		
		actionTable[88][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[88][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[88][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[88][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[88][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[88][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[88][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[88][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[88][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[89][SEMICOLON] = new ActionElement(ActionElement.SHIFT,119);

		actionTable[90][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,120);

		actionTable[91][RPAREN] = new ActionElement(ActionElement.SHIFT,121);
		actionTable[91][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[91][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[91][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[91][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[91][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[91][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[91][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[91][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[91][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[92][RBRACE] = new ActionElement(ActionElement.SHIFT,124);
		actionTable[92][INT] = new ActionElement(ActionElement.SHIFT,29);
		actionTable[92][CHAR] = new ActionElement(ActionElement.SHIFT,30);
		actionTable[92][BOOLEAN] = new ActionElement(ActionElement.SHIFT,31);
		actionTable[92][IF] = new ActionElement(ActionElement.SHIFT,59);
		actionTable[92][WHILE] = new ActionElement(ActionElement.SHIFT,60);
		actionTable[92][RETURN] = new ActionElement(ActionElement.SHIFT,61);
		actionTable[92][SEMICOLON] = new ActionElement(ActionElement.SHIFT,62);
		actionTable[92][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,63);
		actionTable[92][LBRACE] = new ActionElement(ActionElement.SHIFT,64);

		actionTable[93][RBRACE] = new ActionElement(ActionElement.REDUCE,37);
		actionTable[93][INT] = new ActionElement(ActionElement.REDUCE,37);
		actionTable[93][CHAR] = new ActionElement(ActionElement.REDUCE,37);
		actionTable[93][BOOLEAN] = new ActionElement(ActionElement.REDUCE,37);
		actionTable[93][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,37);
		actionTable[93][IF] = new ActionElement(ActionElement.REDUCE,37);
		actionTable[93][WHILE] = new ActionElement(ActionElement.REDUCE,37);
		actionTable[93][RETURN] = new ActionElement(ActionElement.REDUCE,37);
		actionTable[93][SEMICOLON] = new ActionElement(ActionElement.REDUCE,37);
		actionTable[93][LBRACE] = new ActionElement(ActionElement.REDUCE,37);
		actionTable[93][ELSE] = new ActionElement(ActionElement.REDUCE,37);
		
		actionTable[94][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,125);

		actionTable[95][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[95][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[95][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[95][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[95][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[95][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[95][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[95][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[95][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[96][RPAREN] = new ActionElement(ActionElement.SHIFT,127);
		actionTable[96][OR] = new ActionElement(ActionElement.SHIFT,99);

		actionTable[97][RPAREN] = new ActionElement(ActionElement.SHIFT,128);
		actionTable[97][OR] = new ActionElement(ActionElement.SHIFT,99);
		
		actionTable[98][RBRACE] = new ActionElement(ActionElement.REDUCE,45);
		actionTable[98][INT] = new ActionElement(ActionElement.REDUCE,45);
		actionTable[98][CHAR] = new ActionElement(ActionElement.REDUCE,45);
		actionTable[98][BOOLEAN] = new ActionElement(ActionElement.REDUCE,45);
		actionTable[98][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,45);
		actionTable[98][IF] = new ActionElement(ActionElement.REDUCE,45);
		actionTable[98][WHILE] = new ActionElement(ActionElement.REDUCE,45);
		actionTable[98][RETURN] = new ActionElement(ActionElement.REDUCE,45);
		actionTable[98][SEMICOLON] = new ActionElement(ActionElement.REDUCE,45);
		actionTable[98][LBRACE] = new ActionElement(ActionElement.REDUCE,45);
		actionTable[98][ELSE] = new ActionElement(ActionElement.REDUCE,45);
		
		actionTable[99][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[99][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[99][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[99][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[99][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[99][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[99][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[99][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[99][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[100][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[100][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[100][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[100][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[100][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[100][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[100][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[100][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[100][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[101][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[101][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[101][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[101][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[101][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[101][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[101][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[101][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[101][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[102][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[102][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[102][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[102][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[102][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[102][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[102][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[102][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[102][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[103][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[103][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[103][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[103][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[103][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[103][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[103][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[103][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[103][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[104][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[104][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[104][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[104][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[104][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[104][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[104][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[104][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[104][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[105][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[105][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[105][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[105][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[105][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[105][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[105][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[105][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[105][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);
		
		actionTable[106][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[106][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[106][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[106][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[106][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[106][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[106][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[106][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[106][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);
	
		actionTable[107][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[107][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[107][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[107][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[107][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[107][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[108][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[108][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[108][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[108][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[108][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[108][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[109][COMMA] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][SEMICOLON] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][RPAREN] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][OR] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][AND] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][EQ] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][NE] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][GT] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][GE] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][LT] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][LE] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][MINUS] = new ActionElement(ActionElement.REDUCE,63);
		actionTable[109][PLUS] = new ActionElement(ActionElement.REDUCE,63);	
		actionTable[109][PROD] = new ActionElement(ActionElement.SHIFT,112);
		actionTable[109][DIV] = new ActionElement(ActionElement.SHIFT,113);
		actionTable[109][MOD] = new ActionElement(ActionElement.SHIFT,114);
		
		actionTable[110][COMMA] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][SEMICOLON] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][RPAREN] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][OR] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][AND] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][EQ] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][NE] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][GT] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][GE] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][LT] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][LE] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][MINUS] = new ActionElement(ActionElement.REDUCE,64);
		actionTable[110][PLUS] = new ActionElement(ActionElement.REDUCE,64);	
		actionTable[110][PROD] = new ActionElement(ActionElement.SHIFT,112);
		actionTable[110][DIV] = new ActionElement(ActionElement.SHIFT,113);
		actionTable[110][MOD] = new ActionElement(ActionElement.SHIFT,114);
		
		actionTable[111][COMMA] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][SEMICOLON] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][RPAREN] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][OR] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][AND] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][EQ] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][NE] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][GT] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][GE] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][LT] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][LE] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][MINUS] = new ActionElement(ActionElement.REDUCE,65);
		actionTable[111][PLUS] = new ActionElement(ActionElement.REDUCE,65);	
		actionTable[111][PROD] = new ActionElement(ActionElement.SHIFT,112);
		actionTable[111][DIV] = new ActionElement(ActionElement.SHIFT,113);
		actionTable[111][MOD] = new ActionElement(ActionElement.SHIFT,114);
	
		actionTable[112][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[112][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[112][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[112][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[112][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[112][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);
		
		actionTable[113][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[113][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[113][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[113][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[113][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[113][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[114][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[114][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[114][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[114][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[114][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[114][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[115][RPAREN] = new ActionElement(ActionElement.SHIFT,142);
		actionTable[115][OR] = new ActionElement(ActionElement.SHIFT,99);

		actionTable[116][COMMA] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][SEMICOLON] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][RPAREN] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][OR] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][AND] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][EQ] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][NE] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][GT] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][GE] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][LT] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][LE] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][MINUS] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][PLUS] = new ActionElement(ActionElement.REDUCE,81);	
		actionTable[116][PROD] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][DIV] = new ActionElement(ActionElement.REDUCE,81);
		actionTable[116][MOD] = new ActionElement(ActionElement.REDUCE,81);

		actionTable[117][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,143);

		actionTable[118][SEMICOLON] = new ActionElement(ActionElement.SHIFT,144);
		actionTable[118][OR] = new ActionElement(ActionElement.SHIFT,99);
		
		actionTable[119][RBRACE] = new ActionElement(ActionElement.REDUCE,49);
		actionTable[119][INT] = new ActionElement(ActionElement.REDUCE,49);
		actionTable[119][CHAR] = new ActionElement(ActionElement.REDUCE,49);
		actionTable[119][BOOLEAN] = new ActionElement(ActionElement.REDUCE,49);
		actionTable[119][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,49);
		actionTable[119][IF] = new ActionElement(ActionElement.REDUCE,49);
		actionTable[119][WHILE] = new ActionElement(ActionElement.REDUCE,49);
		actionTable[119][RETURN] = new ActionElement(ActionElement.REDUCE,49);
		actionTable[119][SEMICOLON] = new ActionElement(ActionElement.REDUCE,49);
		actionTable[119][LBRACE] = new ActionElement(ActionElement.REDUCE,49);
		actionTable[119][ELSE] = new ActionElement(ActionElement.REDUCE,49);
	
		actionTable[120][LPAREN] = new ActionElement(ActionElement.SHIFT,91);

		actionTable[121][COMMA] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][SEMICOLON] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][RPAREN] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][OR] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][AND] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][EQ] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][NE] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][GT] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][GE] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][LT] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][LE] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][MINUS] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][PLUS] = new ActionElement(ActionElement.REDUCE,83);	
		actionTable[121][PROD] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][DIV] = new ActionElement(ActionElement.REDUCE,83);
		actionTable[121][MOD] = new ActionElement(ActionElement.REDUCE,83);
		
		actionTable[122][RPAREN] = new ActionElement(ActionElement.SHIFT,146);
		actionTable[122][COMMA] = new ActionElement(ActionElement.SHIFT,147);

		actionTable[123][RPAREN] = new ActionElement(ActionElement.REDUCE,85);
		actionTable[123][COMMA] = new ActionElement(ActionElement.REDUCE,85);
		actionTable[123][OR] = new ActionElement(ActionElement.SHIFT,99);

		actionTable[124][RBRACE] = new ActionElement(ActionElement.REDUCE,51);
		actionTable[124][INT] = new ActionElement(ActionElement.REDUCE,51);
		actionTable[124][CHAR] = new ActionElement(ActionElement.REDUCE,51);
		actionTable[124][BOOLEAN] = new ActionElement(ActionElement.REDUCE,51);
		actionTable[124][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,51);
		actionTable[124][IF] = new ActionElement(ActionElement.REDUCE,51);
		actionTable[124][WHILE] = new ActionElement(ActionElement.REDUCE,51);
		actionTable[124][RETURN] = new ActionElement(ActionElement.REDUCE,51);
		actionTable[124][SEMICOLON] = new ActionElement(ActionElement.REDUCE,51);
		actionTable[124][LBRACE] = new ActionElement(ActionElement.REDUCE,51);
		actionTable[124][ELSE] = new ActionElement(ActionElement.REDUCE,51);

		actionTable[125][SEMICOLON] = new ActionElement(ActionElement.REDUCE,40);
		actionTable[125][COMMA] = new ActionElement(ActionElement.REDUCE,40);
		actionTable[125][ASSIGN] = new ActionElement(ActionElement.SHIFT,148);

		actionTable[126][SEMICOLON] = new ActionElement(ActionElement.REDUCE,39);
		actionTable[126][COMMA] = new ActionElement(ActionElement.REDUCE,39);
		actionTable[126][OR] = new ActionElement(ActionElement.SHIFT,99);
		
		actionTable[127][INT] = new ActionElement(ActionElement.SHIFT,29);
		actionTable[127][CHAR] = new ActionElement(ActionElement.SHIFT,30);
		actionTable[127][BOOLEAN] = new ActionElement(ActionElement.SHIFT,31);
		actionTable[127][IF] = new ActionElement(ActionElement.SHIFT,59);
		actionTable[127][WHILE] = new ActionElement(ActionElement.SHIFT,60);
		actionTable[127][RETURN] = new ActionElement(ActionElement.SHIFT,61);
		actionTable[127][SEMICOLON] = new ActionElement(ActionElement.SHIFT,62);
		actionTable[127][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,63);
		actionTable[127][LBRACE] = new ActionElement(ActionElement.SHIFT,64);
		
		actionTable[128][INT] = new ActionElement(ActionElement.SHIFT,29);
		actionTable[128][CHAR] = new ActionElement(ActionElement.SHIFT,30);
		actionTable[128][BOOLEAN] = new ActionElement(ActionElement.SHIFT,31);
		actionTable[128][IF] = new ActionElement(ActionElement.SHIFT,59);
		actionTable[128][WHILE] = new ActionElement(ActionElement.SHIFT,60);
		actionTable[128][RETURN] = new ActionElement(ActionElement.SHIFT,61);
		actionTable[128][SEMICOLON] = new ActionElement(ActionElement.SHIFT,62);
		actionTable[128][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,63);
		actionTable[128][LBRACE] = new ActionElement(ActionElement.SHIFT,64);

		actionTable[129][COMMA] = new ActionElement(ActionElement.REDUCE,53);
		actionTable[129][SEMICOLON] = new ActionElement(ActionElement.REDUCE,53);
		actionTable[129][RPAREN] = new ActionElement(ActionElement.REDUCE,53);
		actionTable[129][OR] = new ActionElement(ActionElement.REDUCE,53);		
		actionTable[129][AND] = new ActionElement(ActionElement.SHIFT,100);

		actionTable[130][COMMA] = new ActionElement(ActionElement.REDUCE,55);
		actionTable[130][SEMICOLON] = new ActionElement(ActionElement.REDUCE,55);
		actionTable[130][RPAREN] = new ActionElement(ActionElement.REDUCE,55);
		actionTable[130][OR] = new ActionElement(ActionElement.REDUCE,55);		
		actionTable[130][AND] = new ActionElement(ActionElement.REDUCE,55);
		
		actionTable[131][COMMA] = new ActionElement(ActionElement.REDUCE,57);
		actionTable[131][SEMICOLON] = new ActionElement(ActionElement.REDUCE,57);
		actionTable[131][RPAREN] = new ActionElement(ActionElement.REDUCE,57);
		actionTable[131][OR] = new ActionElement(ActionElement.REDUCE,57);		
		actionTable[131][AND] = new ActionElement(ActionElement.REDUCE,57);		
		actionTable[131][MINUS] = new ActionElement(ActionElement.SHIFT,107);
		actionTable[131][PLUS] = new ActionElement(ActionElement.SHIFT,108);

		actionTable[132][COMMA] = new ActionElement(ActionElement.REDUCE,58);
		actionTable[132][SEMICOLON] = new ActionElement(ActionElement.REDUCE,58);
		actionTable[132][RPAREN] = new ActionElement(ActionElement.REDUCE,58);
		actionTable[132][OR] = new ActionElement(ActionElement.REDUCE,58);		
		actionTable[132][AND] = new ActionElement(ActionElement.REDUCE,58);		
		actionTable[132][MINUS] = new ActionElement(ActionElement.SHIFT,107);
		actionTable[132][PLUS] = new ActionElement(ActionElement.SHIFT,108);
		
		actionTable[133][COMMA] = new ActionElement(ActionElement.REDUCE,59);
		actionTable[133][SEMICOLON] = new ActionElement(ActionElement.REDUCE,59);
		actionTable[133][RPAREN] = new ActionElement(ActionElement.REDUCE,59);
		actionTable[133][OR] = new ActionElement(ActionElement.REDUCE,59);		
		actionTable[133][AND] = new ActionElement(ActionElement.REDUCE,59);		
		actionTable[133][MINUS] = new ActionElement(ActionElement.SHIFT,107);
		actionTable[133][PLUS] = new ActionElement(ActionElement.SHIFT,108);
		
		actionTable[134][COMMA] = new ActionElement(ActionElement.REDUCE,60);
		actionTable[134][SEMICOLON] = new ActionElement(ActionElement.REDUCE,60);
		actionTable[134][RPAREN] = new ActionElement(ActionElement.REDUCE,60);
		actionTable[134][OR] = new ActionElement(ActionElement.REDUCE,60);		
		actionTable[134][AND] = new ActionElement(ActionElement.REDUCE,60);		
		actionTable[134][MINUS] = new ActionElement(ActionElement.SHIFT,107);
		actionTable[134][PLUS] = new ActionElement(ActionElement.SHIFT,108);
		
		actionTable[135][COMMA] = new ActionElement(ActionElement.REDUCE,61);
		actionTable[135][SEMICOLON] = new ActionElement(ActionElement.REDUCE,61);
		actionTable[135][RPAREN] = new ActionElement(ActionElement.REDUCE,61);
		actionTable[135][OR] = new ActionElement(ActionElement.REDUCE,61);		
		actionTable[135][AND] = new ActionElement(ActionElement.REDUCE,61);		
		actionTable[135][MINUS] = new ActionElement(ActionElement.SHIFT,107);
		actionTable[135][PLUS] = new ActionElement(ActionElement.SHIFT,108);
		
		actionTable[136][COMMA] = new ActionElement(ActionElement.REDUCE,62);
		actionTable[136][SEMICOLON] = new ActionElement(ActionElement.REDUCE,62);
		actionTable[136][RPAREN] = new ActionElement(ActionElement.REDUCE,62);
		actionTable[136][OR] = new ActionElement(ActionElement.REDUCE,62);		
		actionTable[136][AND] = new ActionElement(ActionElement.REDUCE,62);		
		actionTable[136][MINUS] = new ActionElement(ActionElement.SHIFT,107);
		actionTable[136][PLUS] = new ActionElement(ActionElement.SHIFT,108);

		actionTable[137][COMMA] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][SEMICOLON] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][RPAREN] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][OR] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][AND] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][EQ] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][NE] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][GT] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][GE] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][LT] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][LE] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][MINUS] = new ActionElement(ActionElement.REDUCE,67);
		actionTable[137][PLUS] = new ActionElement(ActionElement.REDUCE,67);	
		actionTable[137][PROD] = new ActionElement(ActionElement.SHIFT,112);
		actionTable[137][DIV] = new ActionElement(ActionElement.SHIFT,113);
		actionTable[137][MOD] = new ActionElement(ActionElement.SHIFT,114);

		actionTable[138][COMMA] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][SEMICOLON] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][RPAREN] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][OR] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][AND] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][EQ] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][NE] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][GT] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][GE] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][LT] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][LE] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][MINUS] = new ActionElement(ActionElement.REDUCE,68);
		actionTable[138][PLUS] = new ActionElement(ActionElement.REDUCE,68);	
		actionTable[138][PROD] = new ActionElement(ActionElement.SHIFT,112);
		actionTable[138][DIV] = new ActionElement(ActionElement.SHIFT,113);
		actionTable[138][MOD] = new ActionElement(ActionElement.SHIFT,114);
		
		actionTable[139][COMMA] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][SEMICOLON] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][RPAREN] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][OR] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][AND] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][EQ] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][NE] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][GT] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][GE] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][LT] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][LE] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][MINUS] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][PLUS] = new ActionElement(ActionElement.REDUCE,70);	
		actionTable[139][PROD] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][DIV] = new ActionElement(ActionElement.REDUCE,70);
		actionTable[139][MOD] = new ActionElement(ActionElement.REDUCE,70);

		actionTable[140][COMMA] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][SEMICOLON] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][RPAREN] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][OR] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][AND] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][EQ] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][NE] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][GT] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][GE] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][LT] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][LE] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][MINUS] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][PLUS] = new ActionElement(ActionElement.REDUCE,71);	
		actionTable[140][PROD] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][DIV] = new ActionElement(ActionElement.REDUCE,71);
		actionTable[140][MOD] = new ActionElement(ActionElement.REDUCE,71);
		
		actionTable[141][COMMA] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][SEMICOLON] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][RPAREN] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][OR] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][AND] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][EQ] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][NE] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][GT] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][GE] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][LT] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][LE] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][MINUS] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][PLUS] = new ActionElement(ActionElement.REDUCE,72);	
		actionTable[141][PROD] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][DIV] = new ActionElement(ActionElement.REDUCE,72);
		actionTable[141][MOD] = new ActionElement(ActionElement.REDUCE,72);
		
		actionTable[142][COMMA] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][SEMICOLON] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][RPAREN] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][OR] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][AND] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][EQ] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][NE] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][GT] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][GE] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][LT] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][LE] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][MINUS] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][PLUS] = new ActionElement(ActionElement.REDUCE,75);	
		actionTable[142][PROD] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][DIV] = new ActionElement(ActionElement.REDUCE,75);
		actionTable[142][MOD] = new ActionElement(ActionElement.REDUCE,75);

		actionTable[143][LPAREN] = new ActionElement(ActionElement.SHIFT,91);

		actionTable[144][RBRACE] = new ActionElement(ActionElement.REDUCE,48);
		actionTable[144][INT] = new ActionElement(ActionElement.REDUCE,48);
		actionTable[144][CHAR] = new ActionElement(ActionElement.REDUCE,48);
		actionTable[144][BOOLEAN] = new ActionElement(ActionElement.REDUCE,48);
		actionTable[144][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,48);
		actionTable[144][IF] = new ActionElement(ActionElement.REDUCE,48);
		actionTable[144][WHILE] = new ActionElement(ActionElement.REDUCE,48);
		actionTable[144][RETURN] = new ActionElement(ActionElement.REDUCE,48);
		actionTable[144][SEMICOLON] = new ActionElement(ActionElement.REDUCE,48);
		actionTable[144][LBRACE] = new ActionElement(ActionElement.REDUCE,48);
		actionTable[144][ELSE] = new ActionElement(ActionElement.REDUCE,48);

		actionTable[145][SEMICOLON] = new ActionElement(ActionElement.SHIFT,152);

		actionTable[146][COMMA] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][SEMICOLON] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][RPAREN] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][OR] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][AND] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][EQ] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][NE] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][GT] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][GE] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][LT] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][LE] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][MINUS] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][PLUS] = new ActionElement(ActionElement.REDUCE,84);	
		actionTable[146][PROD] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][DIV] = new ActionElement(ActionElement.REDUCE,84);
		actionTable[146][MOD] = new ActionElement(ActionElement.REDUCE,84);

		actionTable[147][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[147][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[147][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[147][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[147][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[147][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[147][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[147][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[147][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);
		
		actionTable[148][NOT] = new ActionElement(ActionElement.SHIFT,75);
		actionTable[148][MINUS] = new ActionElement(ActionElement.SHIFT,76);
		actionTable[148][PLUS] = new ActionElement(ActionElement.SHIFT,77);
		actionTable[148][LPAREN] = new ActionElement(ActionElement.SHIFT,82);
		actionTable[148][INTEGER_LITERAL] = new ActionElement(ActionElement.SHIFT,83);
		actionTable[148][CHAR_LITERAL] = new ActionElement(ActionElement.SHIFT,84);
		actionTable[148][TRUE] = new ActionElement(ActionElement.SHIFT,85);
		actionTable[148][FALSE] = new ActionElement(ActionElement.SHIFT,86);
		actionTable[148][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,87);

		actionTable[149][RBRACE] = new ActionElement(ActionElement.REDUCE,42);
		actionTable[149][INT] = new ActionElement(ActionElement.REDUCE,42);
		actionTable[149][CHAR] = new ActionElement(ActionElement.REDUCE,42);
		actionTable[149][BOOLEAN] = new ActionElement(ActionElement.REDUCE,42);
		actionTable[149][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,42);
		actionTable[149][IF] = new ActionElement(ActionElement.REDUCE,42);
		actionTable[149][WHILE] = new ActionElement(ActionElement.REDUCE,42);
		actionTable[149][RETURN] = new ActionElement(ActionElement.REDUCE,42);
		actionTable[149][SEMICOLON] = new ActionElement(ActionElement.REDUCE,42);
		actionTable[149][LBRACE] = new ActionElement(ActionElement.REDUCE,42);
		actionTable[149][ELSE] = new ActionElement(ActionElement.SHIFT,155);
		
		actionTable[150][RBRACE] = new ActionElement(ActionElement.REDUCE,44);
		actionTable[150][INT] = new ActionElement(ActionElement.REDUCE,44);
		actionTable[150][CHAR] = new ActionElement(ActionElement.REDUCE,44);
		actionTable[150][BOOLEAN] = new ActionElement(ActionElement.REDUCE,44);
		actionTable[150][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,44);
		actionTable[150][IF] = new ActionElement(ActionElement.REDUCE,44);
		actionTable[150][WHILE] = new ActionElement(ActionElement.REDUCE,44);
		actionTable[150][RETURN] = new ActionElement(ActionElement.REDUCE,44);
		actionTable[150][SEMICOLON] = new ActionElement(ActionElement.REDUCE,44);
		actionTable[150][LBRACE] = new ActionElement(ActionElement.REDUCE,44);
		actionTable[150][ELSE] = new ActionElement(ActionElement.REDUCE,44);
		
		actionTable[151][COMMA] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][SEMICOLON] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][RPAREN] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][OR] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][AND] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][EQ] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][NE] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][GT] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][GE] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][LT] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][LE] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][MINUS] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][PLUS] = new ActionElement(ActionElement.REDUCE,82);	
		actionTable[151][PROD] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][DIV] = new ActionElement(ActionElement.REDUCE,82);
		actionTable[151][MOD] = new ActionElement(ActionElement.REDUCE,82);

		actionTable[152][RBRACE] = new ActionElement(ActionElement.REDUCE,50);
		actionTable[152][INT] = new ActionElement(ActionElement.REDUCE,50);
		actionTable[152][CHAR] = new ActionElement(ActionElement.REDUCE,50);
		actionTable[152][BOOLEAN] = new ActionElement(ActionElement.REDUCE,50);
		actionTable[152][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,50);
		actionTable[152][IF] = new ActionElement(ActionElement.REDUCE,50);
		actionTable[152][WHILE] = new ActionElement(ActionElement.REDUCE,50);
		actionTable[152][RETURN] = new ActionElement(ActionElement.REDUCE,50);
		actionTable[152][SEMICOLON] = new ActionElement(ActionElement.REDUCE,50);
		actionTable[152][LBRACE] = new ActionElement(ActionElement.REDUCE,50);
		actionTable[152][ELSE] = new ActionElement(ActionElement.REDUCE,50);

		actionTable[153][RPAREN] = new ActionElement(ActionElement.REDUCE,86);
		actionTable[153][COMMA] = new ActionElement(ActionElement.REDUCE,86);
		actionTable[153][OR] = new ActionElement(ActionElement.SHIFT,99);

		actionTable[154][SEMICOLON] = new ActionElement(ActionElement.REDUCE,41);
		actionTable[154][COMMA] = new ActionElement(ActionElement.REDUCE,41);
		actionTable[154][OR] = new ActionElement(ActionElement.SHIFT,99);

		actionTable[155][INT] = new ActionElement(ActionElement.SHIFT,29);
		actionTable[155][CHAR] = new ActionElement(ActionElement.SHIFT,30);
		actionTable[155][BOOLEAN] = new ActionElement(ActionElement.SHIFT,31);
		actionTable[155][IF] = new ActionElement(ActionElement.SHIFT,59);
		actionTable[155][WHILE] = new ActionElement(ActionElement.SHIFT,60);
		actionTable[155][RETURN] = new ActionElement(ActionElement.SHIFT,61);
		actionTable[155][SEMICOLON] = new ActionElement(ActionElement.SHIFT,62);
		actionTable[155][IDENTIFIER] = new ActionElement(ActionElement.SHIFT,63);
		actionTable[155][LBRACE] = new ActionElement(ActionElement.SHIFT,64);
		
		actionTable[156][RBRACE] = new ActionElement(ActionElement.REDUCE,43);
		actionTable[156][INT] = new ActionElement(ActionElement.REDUCE,43);
		actionTable[156][CHAR] = new ActionElement(ActionElement.REDUCE,43);
		actionTable[156][BOOLEAN] = new ActionElement(ActionElement.REDUCE,43);
		actionTable[156][IDENTIFIER] = new ActionElement(ActionElement.REDUCE,43);
		actionTable[156][IF] = new ActionElement(ActionElement.REDUCE,43);
		actionTable[156][WHILE] = new ActionElement(ActionElement.REDUCE,43);
		actionTable[156][RETURN] = new ActionElement(ActionElement.REDUCE,43);
		actionTable[156][SEMICOLON] = new ActionElement(ActionElement.REDUCE,43);
		actionTable[156][LBRACE] = new ActionElement(ActionElement.REDUCE,43);
		actionTable[156][ELSE] = new ActionElement(ActionElement.REDUCE,43);
	}
	
	private void initGotoTable() {
		gotoTable = new int[157][37];  // 157 estados, 37 símbolos no terminales
		
		gotoTable[0][S_COMPILATION_UNIT] = 1;
		gotoTable[0][S_IMPORT_CLAUSE_LIST] = 2;
		
		gotoTable[2][S_TINTO_DECL] = 3;
		gotoTable[2][S_IMPORT_CLAUSE] = 4;
		gotoTable[2][S_LIBRARY_DECL] = 6;
		gotoTable[2][S_NATIVE_DECL] = 7;

		gotoTable[14][S_FUNCTION_LIST] = 16;

		gotoTable[15][S_NATIVE_FUNCTION_LIST] = 17;

		gotoTable[16][S_FUNCTION_DECL] = 19;
		gotoTable[16][S_ACCESS] = 20;

		gotoTable[17][S_NATIVE_FUNCTION_DECL] = 24;
		gotoTable[17][S_ACCESS] = 25;
		
		gotoTable[20][S_FUNCTION_TYPE] = 26;
		gotoTable[20][S_TYPE] = 27;

		gotoTable[25][S_FUNCTION_TYPE] = 32;
		gotoTable[25][S_TYPE] = 27;
		
		gotoTable[33][S_ARGUMENT_DECL] = 35;

		gotoTable[34][S_ARGUMENT_DECL] = 37;
		
		gotoTable[35][S_FUNCTION_BODY] = 38;

		gotoTable[36][S_ARGUMENT_LIST] = 41;
		gotoTable[36][S_ARGUMENT] = 42;
		gotoTable[36][S_TYPE] = 43;

		gotoTable[39][S_STATEMENT_LIST] = 45;

		gotoTable[45][S_STATEMENT] = 50;
		gotoTable[45][S_DECL] = 51;
		gotoTable[45][S_ID_STM] = 52;
		gotoTable[45][S_IF_STM] = 53;
		gotoTable[45][S_WHILE_STM] = 54;
		gotoTable[45][S_RETURN_STM] = 55;
		gotoTable[45][S_NO_STM] = 56;
		gotoTable[45][S_BLOCK_STM] = 57;
		gotoTable[45][S_TYPE] = 58;

		gotoTable[47][S_ARGUMENT] = 65;
		gotoTable[47][S_TYPE] = 43;

		gotoTable[58][S_ID_LIST] = 66;

		gotoTable[61][S_EXPR] = 70;
		gotoTable[61][S_AND_EXPR] = 72;
		gotoTable[61][S_REL_EXPR] = 73;
		gotoTable[61][S_SUM_EXPR] = 74;
		gotoTable[61][S_PROD_EXPR] = 78;
		gotoTable[61][S_FACTOR] = 79;
		gotoTable[61][S_LITERAL] = 80;
		gotoTable[61][S_REFERENCE] = 81;
		
		gotoTable[63][S_FUNCTION_CALL] = 89;

		gotoTable[64][S_STATEMENT_LIST] = 92;

		gotoTable[68][S_EXPR] = 96;
		gotoTable[68][S_AND_EXPR] = 72;
		gotoTable[68][S_REL_EXPR] = 73;
		gotoTable[68][S_SUM_EXPR] = 74;
		gotoTable[68][S_PROD_EXPR] = 78;
		gotoTable[68][S_FACTOR] = 79;
		gotoTable[68][S_LITERAL] = 80;
		gotoTable[68][S_REFERENCE] = 81;

		gotoTable[69][S_EXPR] = 97;
		gotoTable[69][S_AND_EXPR] = 72;
		gotoTable[69][S_REL_EXPR] = 73;
		gotoTable[69][S_SUM_EXPR] = 74;
		gotoTable[69][S_PROD_EXPR] = 78;
		gotoTable[69][S_FACTOR] = 79;
		gotoTable[69][S_LITERAL] = 80;
		gotoTable[69][S_REFERENCE] = 81;

		gotoTable[75][S_PROD_EXPR] = 109;
		gotoTable[75][S_FACTOR] = 79;
		gotoTable[75][S_LITERAL] = 80;
		gotoTable[75][S_REFERENCE] = 81;

		gotoTable[76][S_PROD_EXPR] = 110;
		gotoTable[76][S_FACTOR] = 79;
		gotoTable[76][S_LITERAL] = 80;
		gotoTable[76][S_REFERENCE] = 81;
		
		gotoTable[77][S_PROD_EXPR] = 111;
		gotoTable[77][S_FACTOR] = 79;
		gotoTable[77][S_LITERAL] = 80;
		gotoTable[77][S_REFERENCE] = 81;
		
		gotoTable[82][S_EXPR] = 115;
		gotoTable[82][S_AND_EXPR] = 72;
		gotoTable[82][S_REL_EXPR] = 73;
		gotoTable[82][S_SUM_EXPR] = 74;
		gotoTable[82][S_PROD_EXPR] = 78;
		gotoTable[82][S_FACTOR] = 79;
		gotoTable[82][S_LITERAL] = 80;
		gotoTable[82][S_REFERENCE] = 81;

		gotoTable[87][S_FUNCTION_CALL] = 116;

		gotoTable[88][S_EXPR] = 118;
		gotoTable[88][S_AND_EXPR] = 72;
		gotoTable[88][S_REL_EXPR] = 73;
		gotoTable[88][S_SUM_EXPR] = 74;
		gotoTable[88][S_PROD_EXPR] = 78;
		gotoTable[88][S_FACTOR] = 79;
		gotoTable[88][S_LITERAL] = 80;
		gotoTable[88][S_REFERENCE] = 81;

		gotoTable[91][S_EXPR_LIST] = 122;
		gotoTable[91][S_EXPR] = 123;
		gotoTable[91][S_AND_EXPR] = 72;
		gotoTable[91][S_REL_EXPR] = 73;
		gotoTable[91][S_SUM_EXPR] = 74;
		gotoTable[91][S_PROD_EXPR] = 78;
		gotoTable[91][S_FACTOR] = 79;
		gotoTable[91][S_LITERAL] = 80;
		gotoTable[91][S_REFERENCE] = 81;
		
		gotoTable[92][S_STATEMENT] = 50;
		gotoTable[92][S_DECL] = 51;
		gotoTable[92][S_ID_STM] = 52;
		gotoTable[92][S_IF_STM] = 53;
		gotoTable[92][S_WHILE_STM] = 54;
		gotoTable[92][S_RETURN_STM] = 55;
		gotoTable[92][S_NO_STM] = 56;
		gotoTable[92][S_BLOCK_STM] = 57;
		gotoTable[92][S_TYPE] = 58;

		gotoTable[95][S_EXPR] = 126;
		gotoTable[95][S_AND_EXPR] = 72;
		gotoTable[95][S_REL_EXPR] = 73;
		gotoTable[95][S_SUM_EXPR] = 74;
		gotoTable[95][S_PROD_EXPR] = 78;
		gotoTable[95][S_FACTOR] = 79;
		gotoTable[95][S_LITERAL] = 80;
		gotoTable[95][S_REFERENCE] = 81;

		gotoTable[99][S_AND_EXPR] = 129;
		gotoTable[99][S_REL_EXPR] = 73;
		gotoTable[99][S_SUM_EXPR] = 74;
		gotoTable[99][S_PROD_EXPR] = 78;
		gotoTable[99][S_FACTOR] = 79;
		gotoTable[99][S_LITERAL] = 80;
		gotoTable[99][S_REFERENCE] = 81;
		
		gotoTable[100][S_REL_EXPR] = 130;
		gotoTable[100][S_SUM_EXPR] = 74;
		gotoTable[100][S_PROD_EXPR] = 78;
		gotoTable[100][S_FACTOR] = 79;
		gotoTable[100][S_LITERAL] = 80;
		gotoTable[100][S_REFERENCE] = 81;
		
		gotoTable[101][S_SUM_EXPR] = 131;
		gotoTable[101][S_PROD_EXPR] = 78;
		gotoTable[101][S_FACTOR] = 79;
		gotoTable[101][S_LITERAL] = 80;
		gotoTable[101][S_REFERENCE] = 81;
		
		gotoTable[102][S_SUM_EXPR] = 132;
		gotoTable[102][S_PROD_EXPR] = 78;
		gotoTable[102][S_FACTOR] = 79;
		gotoTable[102][S_LITERAL] = 80;
		gotoTable[102][S_REFERENCE] = 81;
		
		gotoTable[103][S_SUM_EXPR] = 133;
		gotoTable[103][S_PROD_EXPR] = 78;
		gotoTable[103][S_FACTOR] = 79;
		gotoTable[103][S_LITERAL] = 80;
		gotoTable[103][S_REFERENCE] = 81;
		
		gotoTable[104][S_SUM_EXPR] = 134;
		gotoTable[104][S_PROD_EXPR] = 78;
		gotoTable[104][S_FACTOR] = 79;
		gotoTable[104][S_LITERAL] = 80;
		gotoTable[104][S_REFERENCE] = 81;
		
		gotoTable[105][S_SUM_EXPR] = 135;
		gotoTable[105][S_PROD_EXPR] = 78;
		gotoTable[105][S_FACTOR] = 79;
		gotoTable[105][S_LITERAL] = 80;
		gotoTable[105][S_REFERENCE] = 81;
		
		gotoTable[106][S_SUM_EXPR] = 136;
		gotoTable[106][S_PROD_EXPR] = 78;
		gotoTable[106][S_FACTOR] = 79;
		gotoTable[106][S_LITERAL] = 80;
		gotoTable[106][S_REFERENCE] = 81;
		
		gotoTable[107][S_PROD_EXPR] = 137;
		gotoTable[107][S_FACTOR] = 79;
		gotoTable[107][S_LITERAL] = 80;
		gotoTable[107][S_REFERENCE] = 81;
	
		gotoTable[108][S_PROD_EXPR] = 138;
		gotoTable[108][S_FACTOR] = 79;
		gotoTable[108][S_LITERAL] = 80;
		gotoTable[108][S_REFERENCE] = 81;
		
		gotoTable[112][S_FACTOR] = 139;
		gotoTable[112][S_LITERAL] = 80;
		gotoTable[112][S_REFERENCE] = 81;

		gotoTable[113][S_FACTOR] = 140;
		gotoTable[113][S_LITERAL] = 80;
		gotoTable[113][S_REFERENCE] = 81;
		
		gotoTable[114][S_FACTOR] = 141;
		gotoTable[114][S_LITERAL] = 80;
		gotoTable[114][S_REFERENCE] = 81;

		gotoTable[120][S_FUNCTION_CALL] = 145;

		gotoTable[127][S_STATEMENT] = 149;
		gotoTable[127][S_DECL] = 51;
		gotoTable[127][S_ID_STM] = 52;
		gotoTable[127][S_IF_STM] = 53;
		gotoTable[127][S_WHILE_STM] = 54;
		gotoTable[127][S_RETURN_STM] = 55;
		gotoTable[127][S_NO_STM] = 56;
		gotoTable[127][S_BLOCK_STM] = 57;
		gotoTable[127][S_TYPE] = 58;
		
		gotoTable[128][S_STATEMENT] = 150;
		gotoTable[128][S_DECL] = 51;
		gotoTable[128][S_ID_STM] = 52;
		gotoTable[128][S_IF_STM] = 53;
		gotoTable[128][S_WHILE_STM] = 54;
		gotoTable[128][S_RETURN_STM] = 55;
		gotoTable[128][S_NO_STM] = 56;
		gotoTable[128][S_BLOCK_STM] = 57;
		gotoTable[128][S_TYPE] = 58;
		
		gotoTable[143][S_FUNCTION_CALL] = 151;
		
		gotoTable[147][S_EXPR] = 153;
		gotoTable[147][S_AND_EXPR] = 72;
		gotoTable[147][S_REL_EXPR] = 73;
		gotoTable[147][S_SUM_EXPR] = 74;
		gotoTable[147][S_PROD_EXPR] = 78;
		gotoTable[147][S_FACTOR] = 79;
		gotoTable[147][S_LITERAL] = 80;
		gotoTable[147][S_REFERENCE] = 81;

		gotoTable[148][S_EXPR] = 154;
		gotoTable[148][S_AND_EXPR] = 72;
		gotoTable[148][S_REL_EXPR] = 73;
		gotoTable[148][S_SUM_EXPR] = 74;
		gotoTable[148][S_PROD_EXPR] = 78;
		gotoTable[148][S_FACTOR] = 79;
		gotoTable[148][S_LITERAL] = 80;
		gotoTable[148][S_REFERENCE] = 81;
		
		gotoTable[155][S_STATEMENT] = 156;
		gotoTable[155][S_DECL] = 51;
		gotoTable[155][S_ID_STM] = 52;
		gotoTable[155][S_IF_STM] = 53;
		gotoTable[155][S_WHILE_STM] = 54;
		gotoTable[155][S_RETURN_STM] = 55;
		gotoTable[155][S_NO_STM] = 56;
		gotoTable[155][S_BLOCK_STM] = 57;
		gotoTable[155][S_TYPE] = 58;
	}
}
