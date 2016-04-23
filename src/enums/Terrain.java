package enums;

public enum Terrain {
	NONE ("X"),	// bottomless pit?
	ROCK ("R"),
	SOIL ("N"),
	GRAVEL ("G"),
	SAND ("S"),
	FLUID ("F");
	
	private final String value;
	
	private Terrain(String value) {
		this.value = value;
	}

	public String getTerString() {
		return value;
	}
}
