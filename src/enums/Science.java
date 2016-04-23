package enums;

public enum Science {
	NONE("N"), 
	RADIOACTIVE("Y"), 
	ORGANIC("O"), 
	MINERAL("M"), 
	ARTIFACT("A"), 
	CRYSTAL("C");

	private final String value;
	
	private Science(String value) {
		this.value = value;
	}

	public String getSciString() {
		return value;
	}
}
