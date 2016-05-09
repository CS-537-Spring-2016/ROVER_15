package enums;

public enum RoverToolType {
	NONE,		// kinda' useless, but there are useless things in the world so ...
	MAST_CAM,			// long range terrain sensor
	RANGE_BOOTER,
	RADIATION_SENSOR,	// for radioactive science locating
	CHEMICAL_SENSOR,	// for organic science locating
	SPECTRAL_SENSOR,	// for crystal science locating
	RADAR_SENSOR,		// for mineral science finding and sensing depth of sandy areas
	DRILL,		// for extracting from rock - also tow other rovers
	EXCAVATOR;	// for extracting from sand, gravel, and soil
	
    public static RoverToolType getEnum(String input){
    	RoverToolType output;
    	switch(input){
    	case "NONE":
    		output = RoverToolType.NONE;
    		break;
    	case "MAST_CAM":
    		output = RoverToolType.MAST_CAM;
    		break;
    	case "RANGE_BOOTER":
    		output = RoverToolType.RANGE_BOOTER;
    		break;
    	case "RADIATION_SENSOR":
    		output = RoverToolType.RADIATION_SENSOR;
    		break;
    	case "CHEMICAL_SENSOR":
    		output = RoverToolType.CHEMICAL_SENSOR;
    		break;
    	case "SPECTRAL_SENSOR":
    		output = RoverToolType.SPECTRAL_SENSOR;
    		break;
    	case "RADAR_SENSOR":
    		output = RoverToolType.RADAR_SENSOR;
    		break;
    	case "DRILL":
    		output = RoverToolType.DRILL;
    		break;
    	case "EXCAVATOR":
    		output = RoverToolType.EXCAVATOR;
    		break;
    	default:
    		output = RoverToolType.NONE;
    	}	
    	return output;
    }
}
