package auxiliares;

public class Transition {
	
	private Expression source;
	private int destination;
	private boolean isReduce;
	
	public Transition() {
		this.isReduce = false;
	}
	
	public Transition(Expression expr, int destination) {
		this.source = expr;
		this.destination = destination;
		this.isReduce = false;
	}
	
	public void setSource(Expression source) {
		this.source = source;
	}
	
	public Expression getSource() {
		return this.source;
	}
	
	public void anadirDestino(int destination) {
		this.destination = destination;
	}
	
	public void addReduce() {
		this.isReduce = true;
	}
	
	public boolean isReduce() {
		return this.isReduce;
	}
	
	public boolean areTransitionsEqual(Transition transition) {
	    return this.destination == transition.getDestination();
	}
	
	public int getDestination() {
		return this.destination;
	}
	
	public String toString() {
	    StringBuilder builder = new StringBuilder();
	    if (isReduce) {
	        builder.append("Reduce R ").append(destination).append("(").append(source.getExpression()).append(")");
	    } else {
	        builder.append(source.getExpression()).append(" -> ").append(destination);
	    }
	    return builder.toString();
	}

}
