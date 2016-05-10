package enums;

public enum RoverDriveType {
	WHEELS, //Rover Wheels
	TREADS, //Rover Treads
	WALKER,
	NONE;
	
    public static RoverDriveType getEnum(String input){
    	RoverDriveType output;
    	switch(input){
    	case "WHEELS":
    		output = RoverDriveType.WHEELS;
    		break;
    	case "TREADS":
    		output = RoverDriveType.TREADS;
    		break;
    	case "WALKER":
    		output = RoverDriveType.WALKER;
    		break;
    	case "NONE":
    		output = RoverDriveType.NONE;
    		break;
    	default:
    		return RoverDriveType.NONE;
    	}	
    	return output;
    }
}
