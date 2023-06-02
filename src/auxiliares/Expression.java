package auxiliares;

public class Expression {

	String expression;
	boolean terminal;

	public Expression(Expression expression) {
		this.expression = expression.expression;
		this.terminal = expression.terminal;
	}
	
	public Expression(String expresion, boolean terminal) {
		this.expression = expresion;
		this.terminal = terminal;
	}
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public boolean isTerminal() {
		return terminal;
	}
	
	public String toString(){
		return expression + " ";
		
		//return expresion + (terminal ? "(terminal)" : "(Noterminal)") +" ";
	}
}
