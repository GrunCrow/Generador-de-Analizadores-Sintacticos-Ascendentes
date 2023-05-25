package parserjj;

public class Regla {
    private String leftHandSide;
    private String[] rightHandSide;

    public Regla(String leftHandSide, String[] rightHandSide) {
        this.setLeftHandSide(leftHandSide);
        this.setRightHandSide(rightHandSide);
    }

	public String getLeftHandSide() {
		return leftHandSide;
	}

	public void setLeftHandSide(String leftHandSide) {
		this.leftHandSide = leftHandSide;
	}

	public String[] getRightHandSide() {
		return rightHandSide;
	}

	public void setRightHandSide(String[] rightHandSide) {
		this.rightHandSide = rightHandSide;
	}

    // Agrega los getters y setters según sea necesario
}
