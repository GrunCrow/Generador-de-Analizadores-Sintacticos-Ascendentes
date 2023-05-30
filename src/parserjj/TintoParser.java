
package parserjj;

import java.io.File;
import java.io.IOException;

import generated.TokenConstants;

/**
 * Analizador sint�ctico de Tinto basado en una gram�tica BNF y LL(1)
 *  
 */
public class TintoParser extends SLRParser implements TokenConstants {

	/**
	 * Analizador l�xico
	 */
	private GrammarLexer lexer;

	/**
	 * Siguiente token de la cadena de entrada
	 */
	private Token nextToken;

	/**
	 * M�todo de an�lisis de un fichero
	 * 
	 * @param file Fichero a analizar
	 * @return Resultado del an�lisis sint�ctico
	 */
	public boolean parse(File file) throws IOException, SintaxException 
	{
		this.lexer = new TintoLexer(file);
		this.nextToken = lexer.getNextToken();
		parseCompilationUnit();
		if(nextToken.getKind() == EOF) return true;
		else return false;
	}

	/**
	 * M�todo que consume un token de la cadena de entrada
	 * @param kind Tipo de token a consumir
	 * @throws SintaxException Si el tipo no coincide con el token 
	 */
	private void match(int kind) throws SintaxException 
	{
		if(nextToken.getKind() == kind) nextToken = lexer.getNextToken();
		else throw new SintaxException(nextToken,kind);
	}

	/**
	 * Analiza el s�mbolo <CompilationUnit>
	 * @throws SintaxException
	 */
	private void parseCompilationUnit() throws SintaxException 
	{
		int[] expected = { IMPORT, LIBRARY, NATIVE };
		switch(nextToken.getKind()) 
		{
			case IMPORT:
			case LIBRARY:
			case NATIVE:
				parseImportClauseList();
				parseTintoDecl();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ImportClauseList>
	 * @throws SintaxException
	 */
	private void parseImportClauseList() throws SintaxException 
	{
		int[] expected = { IMPORT, LIBRARY, NATIVE };
		switch(nextToken.getKind()) 
		{
			case IMPORT:
				parseImportClause();
				parseImportClauseList();
				break;
			case LIBRARY:
			case NATIVE:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ImportClause>
	 * @throws SintaxException
	 */
	private void parseImportClause() throws SintaxException 
	{
		int[] expected = { IMPORT };
		switch(nextToken.getKind()) 
		{
			case IMPORT:
				match(IMPORT);
				match(IDENTIFIER);
				match(SEMICOLON);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <TintoDecl>
	 * @throws SintaxException
	 */
	private void parseTintoDecl() throws SintaxException 
	{
		int[] expected = { LIBRARY, NATIVE };
		switch(nextToken.getKind()) 
		{
			case LIBRARY:
				parseLibraryDecl();
				break;
			case NATIVE:
				parseNativeDecl();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <LibraryDecl>
	 * @throws SintaxException
	 */
	private void parseLibraryDecl() throws SintaxException 
	{
		int[] expected = { LIBRARY };
		switch(nextToken.getKind()) 
		{
			case LIBRARY:
				match(LIBRARY);
				match(IDENTIFIER);
				match(LBRACE);
				parseFunctionList();
				match(RBRACE);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <FunctionList>
	 * @throws SintaxException
	 */
	private void parseFunctionList() throws SintaxException 
	{
		int[] expected = { PUBLIC, PRIVATE };
		switch(nextToken.getKind()) 
		{
			case PUBLIC:
			case PRIVATE:
				parseFunctionDecl();
				parseFunctionList();
				break;
			case RBRACE:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <FunctionDecl>
	 * @throws SintaxException
	 */
	private void parseFunctionDecl() throws SintaxException 
	{
		int[] expected = { PUBLIC, PRIVATE };
		switch(nextToken.getKind()) 
		{
			case PUBLIC:
			case PRIVATE:
				parseAccess();
				parseFunctionType();
				match(IDENTIFIER);
				parseArgumentDecl();
				parseFunctionBody();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <NativeDecl>
	 * @throws SintaxException
	 */
	private void parseNativeDecl() throws SintaxException 
	{
		int[] expected = { NATIVE };
		switch(nextToken.getKind()) 
		{
			case NATIVE:
				match(NATIVE);
				match(IDENTIFIER);
				match(LBRACE);
				parseNativeFunctionList();
				match(RBRACE);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <NativeFunctionList>
	 * @throws SintaxException
	 */
	private void parseNativeFunctionList() throws SintaxException 
	{
		int[] expected = { PUBLIC, PRIVATE };
		switch(nextToken.getKind()) 
		{
			case PUBLIC:
			case PRIVATE:
				parseNativeFunctionDecl();
				parseNativeFunctionList();
				break;
			case RBRACE:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <NativeFunctionDecl>
	 * @throws SintaxException
	 */
	private void parseNativeFunctionDecl() throws SintaxException 
	{
		int[] expected = { PUBLIC, PRIVATE };
		switch(nextToken.getKind()) 
		{
			case PUBLIC:
			case PRIVATE:
				parseAccess();
				parseFunctionType();
				match(IDENTIFIER);
				parseArgumentDecl();
				match(SEMICOLON);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <Access>
	 * @throws SintaxException
	 */
	private void parseAccess() throws SintaxException 
	{
		int[] expected = { PUBLIC, PRIVATE };
		switch(nextToken.getKind()) 
		{
			case PUBLIC:
				match(PUBLIC);
				break;
			case PRIVATE:
				match(PRIVATE);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <FunctionType>
	 * @throws SintaxException
	 */
	private void parseFunctionType() throws SintaxException 
	{
		int[] expected = { VOID, INT, CHAR, BOOLEAN };
		switch(nextToken.getKind()) 
		{
			case VOID:
				match(VOID);
				break;
			case INT:
			case CHAR:
			case BOOLEAN:
				parseType();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <Type>
	 * @throws SintaxException
	 */
	private void parseType() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN };
		switch(nextToken.getKind()) 
		{
			case INT:
				match(INT);
				break;
			case CHAR:
				match(CHAR);
				break;
			case BOOLEAN:
				match(BOOLEAN);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <ArgumentDecl>
	 * @throws SintaxException
	 */
	private void parseArgumentDecl() throws SintaxException 
	{
		int[] expected = { LPAREN };
		switch(nextToken.getKind()) 
		{
			case LPAREN:
				match(LPAREN);
				parseArgumentList();
				match(RPAREN);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ArgumentList>
	 * @throws SintaxException
	 */
	private void parseArgumentList() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN, RPAREN };
		switch(nextToken.getKind()) 
		{
			case INT:
			case CHAR:
			case BOOLEAN:
				parseArgument();
				parseMoreArguments();
				break;
			case RPAREN:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <Argument>
	 * @throws SintaxException
	 */
	private void parseArgument() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN };
		switch(nextToken.getKind()) 
		{
			case INT:
			case CHAR:
			case BOOLEAN:
				parseType();
				match(IDENTIFIER);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <MoreArguments>
	 * @throws SintaxException
	 */
	private void parseMoreArguments() throws SintaxException 
	{
		int[] expected = { COMMA, RPAREN };
		switch(nextToken.getKind()) 
		{
			case COMMA:
				match(COMMA);
				parseArgument();
				parseMoreArguments();
				break;
			case RPAREN:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <FunctionBody>
	 * @throws SintaxException
	 */
	private void parseFunctionBody() throws SintaxException 
	{
		int[] expected = { LBRACE };
		switch(nextToken.getKind()) 
		{
			case LBRACE:
				match(LBRACE);
				parseStatementList();
				match(RBRACE);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <StatementList>
	 * @throws SintaxException
	 */
	private void parseStatementList() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN, IDENTIFIER, IF, WHILE, 
				           RETURN, SEMICOLON, LBRACE, RBRACE };
		switch(nextToken.getKind()) 
		{
			case INT:
			case CHAR:
			case BOOLEAN:
			case IDENTIFIER:
			case IF:
			case WHILE:
			case RETURN:
			case SEMICOLON:
			case LBRACE:
				parseStatement();
				parseStatementList();
				break;
			case RBRACE:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <Statement>
	 * @throws SintaxException
	 */
	private void parseStatement() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN, IDENTIFIER, IF, WHILE, 
				           RETURN, SEMICOLON, LBRACE };
		switch(nextToken.getKind()) 
		{
			case INT:
			case CHAR:
			case BOOLEAN:
				parseDecl();
				break;
			case IDENTIFIER:
				parseIdStm();
				break;
			case IF:
				parseIfStm();
				break;
			case WHILE:
				parseWhileStm();
				break;
			case RETURN:
				parseReturnStm();
				break;
			case SEMICOLON:
				parseNoStm();
				break;
			case LBRACE:
				parseBlockStm();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <Decl>
	 * @throws SintaxException
	 */
	private void parseDecl() throws SintaxException 
	{
		int[] expected = { INT, CHAR, BOOLEAN };
		switch(nextToken.getKind()) 
		{
			case INT:
			case CHAR:
			case BOOLEAN:
				parseType();
				match(IDENTIFIER);
				parseAssignement();
				parseMoreDecl();
				match(SEMICOLON);
				break;

			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <MoreDecl>
	 * @throws SintaxException
	 */
	private void parseMoreDecl() throws SintaxException 
	{
		int[] expected = { COMMA, SEMICOLON };
		switch(nextToken.getKind()) 
		{
			case COMMA:
				match(COMMA);
				match(IDENTIFIER);
				parseAssignement();
				parseMoreDecl();
				break;
			case SEMICOLON:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <Assignement>
	 * @throws SintaxException
	 */
	private void parseAssignement() throws SintaxException 
	{
		int[] expected = { ASSIGN, COMMA, SEMICOLON };
		switch(nextToken.getKind()) 
		{
			case ASSIGN:
				match(ASSIGN);
				parseExpr();
				break;
			case COMMA:
			case SEMICOLON:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <IfStm>
	 * @throws SintaxException
	 */
	private void parseIfStm() throws SintaxException 
	{
		int[] expected = { IF };
		switch(nextToken.getKind()) 
		{
			case IF:
				match(IF);
				match(LPAREN);
				parseExpr();
				match(RPAREN);
				parseStatement();
				parseElseStm();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <ElseStm>
	 * @throws SintaxException
	 */
	private void parseElseStm() throws SintaxException 
	{
		int[] expected = { ELSE, INT, CHAR, BOOLEAN, IDENTIFIER, IF, WHILE, 
		           RETURN, SEMICOLON, LBRACE, RBRACE };
		switch(nextToken.getKind()) 
		{
			case ELSE:
				match(ELSE);
				parseStatement();
				break;
			case INT:
			case CHAR:
			case BOOLEAN:
			case IDENTIFIER:
			case IF:
			case WHILE:
			case RETURN:
			case SEMICOLON:
			case LBRACE:
			case RBRACE:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <WhileStm>
	 * @throws SintaxException
	 */
	private void parseWhileStm() throws SintaxException 
	{
		int[] expected = { WHILE };
		switch(nextToken.getKind()) 
		{
			case WHILE:
				match(WHILE);
				match(LPAREN);
				parseExpr();
				match(RPAREN);
				parseStatement();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <ReturnStm>
	 * @throws SintaxException
	 */
	private void parseReturnStm() throws SintaxException 
	{
		int[] expected = { RETURN };
		switch(nextToken.getKind()) 
		{
			case RETURN:
				match(RETURN);
				parseReturnExpr();
				match(SEMICOLON);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ReturnExpr>
	 * @throws SintaxException
	 */
	private void parseReturnExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				           FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS, SEMICOLON };
		switch(nextToken.getKind()) 
		{
			case NOT:
			case MINUS:
			case PLUS:
			case INTEGER_LITERAL:
			case CHAR_LITERAL:
			case TRUE:
			case FALSE:
			case IDENTIFIER:
			case LPAREN:
				parseExpr();
				break;
			case SEMICOLON:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <NoStm>
	 * @throws SintaxException
	 */
	private void parseNoStm() throws SintaxException 
	{
		int[] expected = { SEMICOLON };
		switch(nextToken.getKind()) 
		{
			case SEMICOLON:
				match(SEMICOLON);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <IdStm>
	 * @throws SintaxException
	 */
	private void parseIdStm() throws SintaxException 
	{
		int[] expected = { IDENTIFIER };
		switch(nextToken.getKind()) 
		{
			case IDENTIFIER:
				match(IDENTIFIER);
				parseIdStmContinue();
				match(SEMICOLON);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <IdStmContinue>
	 * @throws SintaxException
	 */
	private void parseIdStmContinue() throws SintaxException 
	{
		int[] expected = { ASSIGN, LPAREN, DOT };
		switch(nextToken.getKind()) 
		{
			case ASSIGN:
				match(ASSIGN);
				parseExpr();
				break;
			case LPAREN:
				parseFunctionCall();
				break;
			case DOT:
				match(DOT);
				match(IDENTIFIER);
				parseFunctionCall();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <BlockStm>
	 * @throws SintaxException
	 */
	private void parseBlockStm() throws SintaxException 
	{
		int[] expected = { LBRACE };
		switch(nextToken.getKind()) 
		{
			case LBRACE:
				match(LBRACE);
				parseStatementList();
				match(RBRACE);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <Expr>
	 * @throws SintaxException
	 */
	private void parseExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				           FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
			case NOT:
			case MINUS:
			case PLUS:
			case INTEGER_LITERAL:
			case CHAR_LITERAL:
			case TRUE:
			case FALSE:
			case IDENTIFIER:
			case LPAREN:
				parseAndExpr();
				parseMoreOrExpr();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <MoreOrExpr>
	 * @throws SintaxException
	 */
	private void parseMoreOrExpr() throws SintaxException 
	{
		int[] expected = { OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
			case OR:
				match(OR);
				parseAndExpr();
				parseMoreOrExpr();
				break;
			case COMMA:
			case RPAREN:
			case SEMICOLON:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <AndExpr>
	 * @throws SintaxException
	 */
	private void parseAndExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				           FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
			case NOT:
			case MINUS:
			case PLUS:
			case INTEGER_LITERAL:
			case CHAR_LITERAL:
			case TRUE:
			case FALSE:
			case IDENTIFIER:
			case LPAREN:
				parseRelExpr();
				parseMoreAndExpr();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <MoreAndExpr>
	 * @throws SintaxException
	 */
	private void parseMoreAndExpr() throws SintaxException 
	{
		int[] expected = { AND, OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
			case AND:
				match(AND);
				parseRelExpr();
				parseMoreAndExpr();
				break;
			case OR:
			case COMMA:
			case RPAREN:
			case SEMICOLON:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <RelExpr>
	 * @throws SintaxException
	 */
	private void parseRelExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				           FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
			case NOT:
			case MINUS:
			case PLUS:
			case INTEGER_LITERAL:
			case CHAR_LITERAL:
			case TRUE:
			case FALSE:
			case IDENTIFIER:
			case LPAREN:
				parseSumExpr();
				parseMoreRelExpr();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <MoreRelExpr>
	 * @throws SintaxException
	 */
	private void parseMoreRelExpr() throws SintaxException 
	{
		int[] expected = { EQ, NE, GT, GE, LT, LE, AND, OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
			case EQ:
			case NE:
			case GT:
			case GE:
			case LT:
			case LE:
				parseRelOp();
				parseRelExpr();
				break;
			case AND:
			case OR:
			case COMMA:
			case RPAREN:
			case SEMICOLON:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <RelOp>
	 * @throws SintaxException
	 */
	private void parseRelOp() throws SintaxException 
	{
		int[] expected = { EQ, NE, GT, GE, LT, LE };
		switch(nextToken.getKind()) 
		{
			case EQ:
				match(EQ);
				break;
			case NE:
				match(NE);
				break;
			case GT:
				match(GT);
				break;
			case GE:
				match(GE);
				break;
			case LT:
				match(LT);
				break;
			case LE:
				match(LE);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <SumExpr>
	 * @throws SintaxException
	 */
	private void parseSumExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				           FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
			case NOT:
			case MINUS:
			case PLUS:
			case INTEGER_LITERAL:
			case CHAR_LITERAL:
			case TRUE:
			case FALSE:
			case IDENTIFIER:
			case LPAREN:
				parseUnOp();
				parseProdExpr();
				parseMoreSumExpr();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <UnOp>
	 * @throws SintaxException
	 */
	private void parseUnOp() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				           FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
			case NOT:
				match(NOT);
				break;
			case MINUS:
				match(MINUS);
				break;
			case PLUS:
				match(PLUS);
				break;
			case INTEGER_LITERAL:
			case CHAR_LITERAL:
			case TRUE:
			case FALSE:
			case IDENTIFIER:
			case LPAREN:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <MoreSumExpr>
	 * @throws SintaxException
	 */
	private void parseMoreSumExpr() throws SintaxException 
	{
		int[] expected = { PLUS, MINUS, EQ, NE, GT, GE, LT, LE, 
				           AND, OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
			case PLUS:
			case MINUS:
				parseSumOp();
				parseProdExpr();
				parseMoreSumExpr();
				break;
			case EQ:
			case NE:
			case GT:
			case GE:
			case LT:
			case LE:
			case AND:
			case OR:
			case COMMA:
			case RPAREN:
			case SEMICOLON:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <SumOp>
	 * @throws SintaxException
	 */
	private void parseSumOp() throws SintaxException 
	{
		int[] expected = { MINUS, PLUS };
		switch(nextToken.getKind()) 
		{
			case MINUS:
				match(MINUS);
				break;
			case PLUS:
				match(PLUS);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <ProdExpr>
	 * @throws SintaxException
	 */
	private void parseProdExpr() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				           FALSE, IDENTIFIER, LPAREN };
		switch(nextToken.getKind()) 
		{
			case INTEGER_LITERAL:
			case CHAR_LITERAL:
			case TRUE:
			case FALSE:
			case IDENTIFIER:
			case LPAREN:
				parseFactor();
				parseMoreProdExpr();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <MoreProdExpr>
	 * @throws SintaxException
	 */
	private void parseMoreProdExpr() throws SintaxException 
	{
		int[] expected = { PROD, DIV, MOD, PLUS, MINUS, EQ, NE, GT, GE, LT, LE, 
				           AND, OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
			case PROD:
			case DIV:
			case MOD:
				parseProdOp();
				parseFactor();
				parseMoreProdExpr();
				break;
			case PLUS:
			case MINUS:
			case EQ:
			case NE:
			case GT:
			case GE:
			case LT:
			case LE:
			case AND:
			case OR:
			case COMMA:
			case RPAREN:
			case SEMICOLON:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <ProdOp>
	 * @throws SintaxException
	 */
	private void parseProdOp() throws SintaxException 
	{
		int[] expected = { PROD, DIV, MOD };
		switch(nextToken.getKind()) 
		{
			case PROD:
				match(PROD);
				break;
			case DIV:
				match(DIV);
				break;
			case MOD:
				match(MOD);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <Factor>
	 * @throws SintaxException
	 */
	private void parseFactor() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
				           FALSE, IDENTIFIER, LPAREN };
		switch(nextToken.getKind()) 
		{
			case INTEGER_LITERAL:
			case CHAR_LITERAL:
			case TRUE:
			case FALSE:
				parseLiteral();
				break;
			case IDENTIFIER:
				parseReference();
				break;
			case LPAREN:
				match(LPAREN);
				parseExpr();
				match(RPAREN);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <Literal>
	 * @throws SintaxException
	 */
	private void parseLiteral() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, FALSE };
		switch(nextToken.getKind()) 
		{
			case INTEGER_LITERAL:
				match(INTEGER_LITERAL);
				break;
			case CHAR_LITERAL:
				match(CHAR_LITERAL);
				break;
			case TRUE:
				match(TRUE);
				break;
			case FALSE:
				match(FALSE);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <Reference>
	 * @throws SintaxException
	 */
	private void parseReference() throws SintaxException 
	{
		int[] expected = { IDENTIFIER };
		switch(nextToken.getKind()) 
		{
			case IDENTIFIER:
				match(IDENTIFIER);
				parseReferenceContinue();
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <ReferenceContinue>
	 * @throws SintaxException
	 */
	private void parseReferenceContinue() throws SintaxException 
	{
		int[] expected = { LPAREN, DOT, PROD, DIV, MOD, PLUS, MINUS, EQ, 
						NE, GT, GE, LT, LE, AND, OR, COMMA, RPAREN, SEMICOLON };
		switch(nextToken.getKind()) 
		{
			case LPAREN:
				parseFunctionCall();
				break;
			case DOT:
				match(DOT);
				match(IDENTIFIER);
				parseFunctionCall();
				break;
			case PROD:
			case DIV:
			case MOD:
			case PLUS:
			case MINUS:
			case EQ:
			case NE:
			case GT:
			case GE:
			case LT:
			case LE:
			case AND:
			case OR:
			case COMMA:
			case RPAREN:
			case SEMICOLON:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <FunctionCall>
	 * @throws SintaxException
	 */
	private void parseFunctionCall() throws SintaxException 
	{
		int[] expected = { LPAREN };
		switch(nextToken.getKind()) 
		{
			case LPAREN:
				match(LPAREN);
				parseExprList();
				match(RPAREN);
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}

	/**
	 * Analiza el s�mbolo <ExprList>
	 * @throws SintaxException
	 */
	private void parseExprList() throws SintaxException 
	{
		int[] expected = { INTEGER_LITERAL, CHAR_LITERAL, TRUE, 
		           FALSE, IDENTIFIER, LPAREN, NOT, MINUS, PLUS , RPAREN };
		switch(nextToken.getKind()) 
		{
			case NOT:
			case MINUS:
			case PLUS:
			case INTEGER_LITERAL:
			case CHAR_LITERAL:
			case TRUE:
			case FALSE:
			case IDENTIFIER:
			case LPAREN:
				parseExpr();
				parseMoreExpr();
				break;
			case RPAREN:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
	
	/**
	 * Analiza el s�mbolo <MoreExpr>
	 * @throws SintaxException
	 */
	private void parseMoreExpr() throws SintaxException 
	{
		int[] expected = { COMMA, RPAREN };
		switch(nextToken.getKind()) 
		{
			case COMMA:
				match(COMMA);
				parseExpr();
				parseMoreExpr();
				break;
			case RPAREN:
				break;
			default:
				throw new SintaxException(nextToken,expected);
		}
	}
}
