package enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// thank you to this post: http://stackoverflow.com/questions/3054247/how-to-define-properties-for-enum-items

public enum RoverName {
	// (Drive type, accessory slot 1, accessory slot 2)
	NONE,
	
	ROVER_01 ("TREADS", "SPECTRAL_SENSOR", "CHEMICAL_SENSOR"),
	ROVER_02 ("WHEELS", "RADIATION_SENSOR", "SPECTRAL"),
	ROVER_03 ("WHEELS", "RADIATION_SENSOR", "SPECTRAL"),
	ROVER_04 ("WHEELS", "RADIATION_SENSOR", "SPECTRAL"),
	ROVER_05 ("WHEELS", "RADIATION_SENSOR", "CHEMICAL_SENSOR"),
	ROVER_06 ("WHEELS", "RADIATION_SENSOR", "CHEMICAL_SENSOR"),
	ROVER_07 ("WHEELS", "RADIATION_SENSOR", "CHEMICAL_SENSOR"),
	ROVER_08 ("WHEELS", "RADIATION_SENSOR", "CHEMICAL_SENSOR"),
	ROVER_09 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	ROVER_10 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	ROVER_11 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	ROVER_12 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	ROVER_13 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	ROVER_14 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	ROVER_15 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	ROVER_16 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	ROVER_17 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	ROVER_18 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	ROVER_19 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	ROVER_20 ("WHEELS", "RADIATION_SENSOR", "DRILL"),
	
	// sample test rovers
	ROVER_00 ("WHEELS", "RADIATION_SENSOR", "RADAR_SENSOR"),
	ROVER_99 ("TREADS", "SPECTRAL_SENSOR", "CHEMICAL_SENSOR");
	
    private final List<String> members;
    private RoverName(String... members){
        this.members=Arrays.asList(members);
    }
    public List<String> getMembers(){
        // defensive copy, because the original list is mutable
        return new ArrayList<String>(members);
    }
    
    public static RoverName getEnum(String input){
    	RoverName output;
    	
    	switch(input){
    	case "ROVER_00":
    		 System.out.println("ROVERNAME: case ROVER_00");
    		output = RoverName.ROVER_00;
    		break;
    	case "ROVER_01":
    		output = RoverName.ROVER_01;
    		break;
    	case "ROVER_02":
    		output = RoverName.ROVER_02;
    		break;
    	case "ROVER_03":
    		output = RoverName.ROVER_03;
    		break;
    	case "ROVER_04":
    		output = RoverName.ROVER_04;
    		break;
    	case "ROVER_05":
    		output = RoverName.ROVER_05;
    		break;
    	case "ROVER_06":
    		output = RoverName.ROVER_06;
    		break;
    	case "ROVER_07":
    		output = RoverName.ROVER_07;
    		break;
    	case "ROVER_08":
    		output = RoverName.ROVER_08;
    		break;
    	case "ROVER_09":
    		output = RoverName.ROVER_09;
    		break;
    	case "ROVER_10":
    		output = RoverName.ROVER_10;
    		break;
    	case "ROVER_11":
    		output = RoverName.ROVER_11;
    		break;
    	case "ROVER_12":
    		output = RoverName.ROVER_12;
    		break;    		
    	case "ROVER_13":
    		output = RoverName.ROVER_13;
    		break;
    	case "ROVER_14":
    		output = RoverName.ROVER_14;
    		break;
    	case "ROVER_15":
    		output = RoverName.ROVER_15;
    		break;
    	case "ROVER_16":
    		output = RoverName.ROVER_16;
    		break;
    	case "ROVER_17":
    		output = RoverName.ROVER_17;
    		break;
    	case "ROVER_18":
    		output = RoverName.ROVER_18;
    		break;
    	case "ROVER_19":
    		output = RoverName.ROVER_19;
    		break;
    	case "ROVER_20":
    		output = RoverName.ROVER_20;
    		break;
    	case "ROVER_99":
    		output = RoverName.ROVER_99;
    		break;
    	default:
    		output = RoverName.NONE;
    	}	
    	return output;
    }
}
